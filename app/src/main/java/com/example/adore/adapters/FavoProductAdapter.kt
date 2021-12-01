package com.example.adore.adapters

import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adore.databinding.FavoProductBinding
import com.example.adore.models.entities.Product
import com.example.adore.models.enums.CustomLabel
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.util.AdoreLogic
import com.example.adore.util.Constants
import kotlinx.android.synthetic.main.favo_product.view.*

class FavoProductAdapter(
    val viewModel: ProductsViewModel
    ): RecyclerView.Adapter<FavoProductAdapter.FavoProductViewHolder>() {

    class FavoProductViewHolder(val binding: FavoProductBinding): RecyclerView.ViewHolder(binding.root){
        companion object{
            fun from(parent: ViewGroup): FavoProductViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoProductBinding.inflate(layoutInflater, parent, false)
                return FavoProductViewHolder(binding)
            }
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ) = oldItem._id == newItem._id

        override fun areContentsTheSame(oldItem: Product, newItem: Product)= oldItem==newItem

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FavoProductViewHolder.from(parent)

    override fun onBindViewHolder(holder: FavoProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.binding.apply {
            Glide.with(holder.itemView).load(product.imageUrl).into(ivProductImage)
            tvProductName.text = product.name
            val price = Constants.CURRENCY + product.price.toString()
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
            ivRemoveItem.setOnClickListener {
                viewModel.removeFavoItem(product._id)
            }
        }
    }

    private var onItemClickListener: ((Product) -> Unit)? = null

    fun setOnItemClickListener(listener:(Product) -> Unit){
        onItemClickListener = listener
    }

    override fun getItemCount() = differ.currentList.size
}