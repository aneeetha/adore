package com.example.adore.adapters

import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adore.databinding.ProductPreviewBinding
import com.example.adore.models.entities.Product
import com.example.adore.models.enums.CustomLabel
import com.example.adore.util.AdoreLogic
import com.example.adore.util.Constants
import kotlinx.android.synthetic.main.product_preview.view.*

class ProductsAdapter(): RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>(){

    private val differCallback = object: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ) = oldItem._id == newItem._id

        override fun areContentsTheSame(oldItem: Product, newItem: Product)= oldItem==newItem

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder = ProductViewHolder.from(parent)

    override fun getItemCount(): Int = differ.currentList.size


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        val currency = "Rs. "
        holder.binding.apply {
            Glide.with(holder.itemView).load(product.imageUrl).into(ivProductImage)
            tvProductName.text = product.name
            val price = currency + product.price.toString()
            tvProductPrice.text = price
            val discount: Int = AdoreLogic.getDiscount(product.customLabels)
            if(discount!=0){
                tvProductPriceDiscounted.visibility = View.VISIBLE
                val discountedPrice = Constants.CURRENCY + (product.price - (product.price.times(discount).div(100))).toString()
                tvProductPriceDiscounted.text = discountedPrice
                tvProductPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                tvProductPrice.typeface = Typeface.DEFAULT
                tvProductPrice.textSize = 11F
            }
            tvProductDescription.text = product.description
            holder.itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(product) }
            }
        }
    }

    private var onItemClickListener: ((Product) -> Unit)? = null

    fun setOnItemClickListener(listener:(Product) -> Unit){
        onItemClickListener = listener
    }


    class ProductViewHolder(val binding: ProductPreviewBinding): RecyclerView.ViewHolder(binding.root){
        companion object{
            fun from(parent: ViewGroup): ProductViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProductPreviewBinding.inflate(layoutInflater, parent, false)
                return ProductViewHolder(binding)
            }
        }
    }
}