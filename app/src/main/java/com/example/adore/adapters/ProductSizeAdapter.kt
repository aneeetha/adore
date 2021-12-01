package com.example.adore.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adore.R
import com.example.adore.models.enums.DressSize
import com.example.adore.models.Stock
import kotlinx.android.synthetic.main.size_item.view.*

class ProductSizeAdapter() : ListAdapter<Stock, ProductSizeAdapter.TextViewHolder>(SizeViewDiffCallback()){

    private var selectedSizePosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder = TextViewHolder.from(parent)

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.apply {
            itemView.tv_size_item.text = currentItem.size.name
            if(currentItem.availableCount>1){
                if(selectedSizePosition != bindingAdapterPosition)  unCheckSizeView(itemView)
                itemView.setOnClickListener {
                    onItemClickListener?.let{
                        setSizeViewChecked(itemView)
                        selectedSizePosition = bindingAdapterPosition
                        it(currentItem.size)
                        notifyItemRangeChanged(0, position)
                        notifyItemRangeChanged(position+1, currentList.size)
                    }
                }
            }else{
                itemView.setBackgroundResource(R.drawable.faded_text_view_bg)
                itemView.isEnabled=false
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

    private fun setSizeViewChecked(view: View){
        view.setBackgroundResource(R.drawable.highlight_text_view_bg)
    }

    private fun unCheckSizeView(view: View){
        view.setBackgroundResource(R.drawable.text_view_bg)
    }

    private fun setSizeNotAvailable(view: View){
        view.setBackgroundResource(R.drawable.faded_text_view_bg)
    }
}