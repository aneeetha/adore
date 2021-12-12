package com.example.adore.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adore.R
import com.example.adore.models.enums.DressSize
import com.example.adore.models.dataClasses.Stock
import kotlinx.android.synthetic.main.size_item.view.*

class ProductSizeAdapter() : ListAdapter<Stock, ProductSizeAdapter.TextViewHolder>(SizeViewDiffCallback()){

    private var selectedSizePosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder = TextViewHolder.from(parent)

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.apply {
            itemView.tv_size_item.text = currentItem.size.name
            if(currentItem.availableCount>1){
                if(selectedSizePosition != bindingAdapterPosition)  itemView.unCheckSizeView()
                itemView.setOnClickListener {
                    onItemClickListener?.let{
                        itemView.setSizeViewChecked()
                        selectedSizePosition = bindingAdapterPosition
                        it(currentItem.size)
                        Log.e("SizeAdapter", "${currentItem.size}")
                        notifyItemRangeChanged(0, position)
                        notifyItemRangeChanged(position+1, currentList.size)
                    }
                }
            }else{
                itemView.setSizeNotAvailable()
                //val newList = currentList.subList(0, position) + currentList.subList(position+1, currentList.size)
                //submitList(newList)
            }

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

    class SizeViewDiffCallback: DiffUtil.ItemCallback<Stock>() {
        override fun areItemsTheSame(oldItem: Stock, newItem: Stock): Boolean = oldItem.id==newItem.id

        override fun areContentsTheSame(oldItem: Stock, newItem: Stock): Boolean = oldItem==newItem
    }

    private fun View.setSizeViewChecked(){
        setBackgroundResource(R.drawable.highlight_text_view_bg)
    }

    private fun View.unCheckSizeView(){
        setBackgroundResource(R.drawable.text_view_bg)
    }

    private fun View.setSizeNotAvailable(){
        setBackgroundResource(R.drawable.faded_text_view_bg)
        isEnabled = false
    }
}