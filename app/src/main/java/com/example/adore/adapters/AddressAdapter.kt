package com.example.adore.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adore.R
import com.example.adore.models.entities.Address
import kotlinx.android.synthetic.main.list_text_view.view.*

class AddressAdapter: ListAdapter<Address, AddressAdapter.TextViewHolder>(SizeViewDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TextViewHolder.from(parent)

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        val currentAddress = getItem(position)
        holder.itemView.tv_address_type_list.apply{
            text = currentAddress.addressType
            setOnClickListener {
                onItemClickListener?.let{
                    it(currentAddress)
                }
            }
        }
    }

    private var onItemClickListener: ((Address) -> Unit)? = null

    fun setOnItemClickListener(listener:(Address) -> Unit){
        onItemClickListener = listener
    }

    class TextViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_text_view, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class SizeViewDiffCallback: DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean = oldItem.addressId==newItem.addressId

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean = oldItem==newItem
    }

}