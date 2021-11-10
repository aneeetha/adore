package com.example.adore.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adore.R
import com.example.adore.models.Product
import kotlinx.android.synthetic.main.product_preview.view.*

class ProductsAdapter: RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>(){

    inner class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product) = oldItem._id == newItem._id


        override fun areContentsTheSame(oldItem: Product, newItem: Product)= oldItem==newItem

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder = ProductViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.product_preview,
            parent,
            false
        )
    )

    override fun getItemCount(): Int = differ.currentList.size


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        val currency = "Rs. "
        holder.itemView.apply {
            Glide.with(this).load(product.imageUrl).into(iv_product_image)
            tv_name.text = product.name
            val price = currency + product.price.toString()
            tv_price.text = price
            setOnClickListener {
                onItemClickListener?.let { it(product) }
            }
        }
    }

    private var onItemClickListener: ((Product) -> Unit)? = null

    fun setOnItemClickListener(listener:(Product) -> Unit){
        onItemClickListener = listener
    }
}