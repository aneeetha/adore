package com.example.adore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adore.databinding.ProductPreviewBinding
import com.example.adore.models.Product
import kotlinx.android.synthetic.main.product_preview.view.*

class ProductsAdapter(private val clickListener: ProductClickListener): RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>(){

    private val differCallback = object: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product) = oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Product, newItem: Product)= oldItem==newItem

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder = ProductViewHolder.from(parent)

    override fun getItemCount(): Int = differ.currentList.size


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        val currency = "Rs. "
        holder.bind(product, clickListener)
        holder.itemView.apply {
            Glide.with(this).load(product.imageUrl).into(iv_product_image)
            tv_product_name.text = product.name
            val price = currency + product.price.toString()
            tv_product_price.text = price
            tv_product_description.text = product.description
            setOnClickListener {
                onItemClickListener?.let {
                    it(product) }
            }
        }
    }

    private var onItemClickListener: ((Product) -> Unit)? = null

    fun setOnItemClickListener(listener:(Product) -> Unit){
        onItemClickListener = listener
    }

    class ProductClickListener(val clickListener: (productId: String)->Unit){
        fun onClick(product: Product) = clickListener(product.id)
    }

    class ProductViewHolder(val binding: ProductPreviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Product, clickListener: ProductClickListener){
            binding.apply {
                product = item
                this.clickListener = clickListener
                executePendingBindings()
            }
        }

        companion object{
            fun from(parent: ViewGroup): ProductViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProductPreviewBinding.inflate(layoutInflater, parent, false)
                return ProductViewHolder(binding)
            }
        }
    }
}