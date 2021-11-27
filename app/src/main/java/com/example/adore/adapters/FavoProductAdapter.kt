package com.example.adore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adore.databinding.FavoProductBinding
import com.example.adore.models.entities.Product
import com.example.adore.ui.viewmodels.ProductsViewModel
import kotlinx.android.synthetic.main.favo_product.view.*

class FavoProductAdapter(
    val viewModel: ProductsViewModel
    ): RecyclerView.Adapter<FavoProductAdapter.FavoProductViewHolder>() {

    class FavoProductViewHolder(private val binding: FavoProductBinding): RecyclerView.ViewHolder(binding.root){
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
        val currency = "Rs. "
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
            iv_remove_item.setOnClickListener {
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