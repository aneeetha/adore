package com.example.adore.adapters

import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adore.R
import com.example.adore.databinding.CartItemPreviewBinding
import com.example.adore.models.dataClasses.CartItem
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.util.Constants

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
        holder.binding.apply {
            val price = Constants.CURRENCY + currentItem.productDetails.price.toString()
            val sellingPrice = Constants.CURRENCY + currentItem.sellingPrice.toString()
            val count = if(currentItem.productDetails.availableCount>5) 5 else currentItem.productDetails.availableCount
            val quantity = Array(count) { "${it + 1}" }
            val arrayAdapter = ArrayAdapter(holder.itemView.context, R.layout.dropdown_item, quantity)
            tvAutoComplete.setAdapter(arrayAdapter)
            Glide.with(holder.itemView).load(currentItem.productDetails.imageUrl).into(ivProductImage)
            tvProductName.text = currentItem.productDetails.name
            tvProductDescription.text = currentItem.productDetails.description
            tvProductPrice.text = price
            if(currentItem.discount>0){
                tvProductPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                tvProductPrice.typeface = Typeface.DEFAULT
                tvProductPrice.textSize = 11F
                tvProductPriceDiscounted.text = sellingPrice
            }else{
                tvProductPriceDiscounted.visibility = View.GONE
            }
            tvSelectedSize.text = holder.itemView.context.getString(R.string.size_view, currentItem.selectedSize.name)
            tvAutoComplete.setText(currentItem.quantity.toString(), false)


            tvAutoComplete.setOnItemClickListener { _, _, i, _ ->
                arrayAdapter.getItem(i)?.let { quantity ->
                    if(quantityCheck(quantity.toInt())){
                        viewModel.cartItemQuantityChanged(quantity.toInt(), currentItem._id)
                    }
                }
            }

            ivCartItemRemove.setOnClickListener {
                viewModel.removeCartItem(currentItem._id)
            }
        }
    }

    private fun quantityCheck(quantity: Int)= quantity <= 5


    class CartItemDifferCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
            oldItem._id == newItem._id

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
            oldItem == newItem
    }
}