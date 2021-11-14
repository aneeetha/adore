package com.example.adore.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.adore.R
import com.example.adore.models.DressSize
import com.example.adore.models.Stock
import kotlinx.android.synthetic.main.size_item.view.*

class ProductSizeAdapter(private val stock: List<Stock>) : RecyclerView.Adapter<ProductSizeAdapter.TextViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder = TextViewHolder.from(parent)

    override fun getItemCount(): Int = stock.size

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        stock[position].run {
            holder.itemView.tv_size_item.text = size.toString()
            if(availableCount>1){
                holder.itemView.setOnClickListener {
                    onItemClickListener?.let {
                        holder.itemView.setBackgroundResource(R.drawable.faded_text_view_bg)
                        it(size)}
                }
            }else{
                holder.itemView.setBackgroundResource(R.drawable.faded_text_view_bg)  }
        }
    }

    private var onItemClickListener: ((DressSize) -> Unit)? = null

    fun setOnItemClickListener(listener:(DressSize) -> Unit){
        onItemClickListener = listener
    }

    class TextViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.size_item, parent, false)
                return TextViewHolder(view)
            }
        }
    }


}