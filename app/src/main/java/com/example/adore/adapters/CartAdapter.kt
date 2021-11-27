package com.example.adore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adore.R
import com.example.adore.databinding.CartItemPreviewBinding
import com.example.adore.models.CartItem
import com.example.adore.models.entities.Product
import com.example.adore.ui.viewmodels.ProductsViewModel
import kotlinx.android.synthetic.main.cart_item_preview.view.*

class CartAdapter(
    val viewModel: ProductsViewModel
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartItemDifferCallback()) {

    class CartViewHolder(val binding: CartItemPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): CartViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CartItemPreviewBinding.inflate(layoutInflater, parent, false)
                return CartViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder =
        CartViewHolder.from(parent)

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.itemView.apply {
            val quantity = Array<String>(currentItem.productDetails.availableCount) { "${it + 1}" }
            val arrayAdapter = ArrayAdapter(context, R.layout.dropdown_item, quantity)
            tv_auto_complete.setAdapter(arrayAdapter)
            Glide.with(this).load(currentItem.productDetails.imageUrl).into(iv_product_image)
            tv_product_name.text = currentItem.productDetails.name
            tv_product_description.text = currentItem.productDetails.description
            tv_product_price.text = currentItem.productDetails.price.toString()
            tv_selected_size.text =
                context.getString(R.string.size_view, currentItem.selectedSize.name)
            tv_auto_complete.setText(currentItem.quantity.toString(), false)


            tv_auto_complete.setOnItemClickListener { adapterView, view, i, l ->
                arrayAdapter.getItem(i)?.let { quantity ->
                    if(quantityCheck(quantity.toInt())){
                        viewModel.cartItemQuantityChanged(quantity.toInt(), currentItem._id)
                    }
                }
            }

            iv_cart_item_remove.setOnClickListener {
                viewModel.removeCartItem(currentItem._id)
            }
        }
    }

    private fun quantityCheck(quantity: Int)= quantity < 5


    class CartItemDifferCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
            oldItem._id == newItem._id

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
            oldItem == newItem
    }
}