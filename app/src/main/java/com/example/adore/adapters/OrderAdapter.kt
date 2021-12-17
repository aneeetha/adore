package com.example.adore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adore.R
import com.example.adore.databinding.OrderItemBinding
import com.example.adore.models.dataClasses.OrderProductDetails
import com.example.adore.models.enums.OrderStatus
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.util.Constants

class OrderAdapter() : ListAdapter<OrderProductDetails, OrderAdapter.OrderViewHolder>(OrderItemDifferCallback()) {

    class OrderViewHolder(val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): OrderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OrderItemBinding.inflate(layoutInflater, parent, false)
                return OrderViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OrderViewHolder.from(parent)

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.apply {
            Glide.with(holder.itemView).load(currentItem.imageUrl).into(ivProductImage)
            tvProductName.text = currentItem.name
            tvProductDescription.text = currentItem.description

            val price = Constants.CURRENCY + currentItem.sellingPrice.toString()
            tvProductPrice.text = price

            tvProductQuantity.text = holder.itemView.context.getString(R.string.quantity, currentItem.quantity.toString())
            tvSelectedSize.text = holder.itemView.context.getString(R.string.size_view, currentItem.selectedSize.name)

            holder.itemView.context.getString(R.string.order_status, OrderStatus.valueOf(currentItem.orderStatus)).let {
                tvOrderStatus.text = HtmlCompat.fromHtml(String.format(it, "placeholder1"), HtmlCompat.FROM_HTML_MODE_COMPACT)
            }
        }

    }

    class OrderItemDifferCallback : DiffUtil.ItemCallback<OrderProductDetails>() {
        override fun areItemsTheSame(oldItem: OrderProductDetails, newItem: OrderProductDetails): Boolean =
            oldItem.sku == newItem.sku

        override fun areContentsTheSame(oldItem: OrderProductDetails, newItem: OrderProductDetails): Boolean =
            oldItem == newItem
    }
}