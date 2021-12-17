package com.example.adore.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adore.R
import com.example.adore.models.entities.Address
import kotlinx.android.synthetic.main.list_user_address.view.*

class AddressAdapter: ListAdapter<Address, AddressAdapter.TextViewHolder>(SizeViewDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TextViewHolder.from(parent)

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        val currentAddress = getItem(position)
        holder.itemView.apply{
            tv_address_type.text = currentAddress.addressType
            tv_address_type.setOnClickListener {
                onItemClickListener?.let{
                    it(currentAddress)
                }
            }

           iv_delete_icon.setOnClickListener {
               onDeleteIconClickListener?.let {
                   it(currentAddress)
               }
           }
        }
    }

    private var onItemClickListener: ((Address) -> Unit)? = null

    fun setOnItemClickListener(listener:(Address) -> Unit){
        onItemClickListener = listener
    }

    private var onDeleteIconClickListener: ((Address) -> Unit)? = null

    fun setOnDeleteIconClickListener(listener:(Address) -> Unit){
        onDeleteIconClickListener = listener
    }

    class TextViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_user_address, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class SizeViewDiffCallback: DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean = oldItem.addressId==newItem.addressId

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean = oldItem==newItem
    }

}