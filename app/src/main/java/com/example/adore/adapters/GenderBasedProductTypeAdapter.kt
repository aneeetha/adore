package com.example.adore.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.adore.databinding.ProductTypeListItemBinding
import com.example.adore.models.enums.ProductType

class GenderBasedProductTypeAdapter(
    private val productTypes: List<ProductType>,
    private val colorsList: List<String>
    ): RecyclerView.Adapter<GenderBasedProductTypeAdapter.ProductTypeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductTypeViewHolder.from(parent)

    override fun onBindViewHolder(holder: ProductTypeViewHolder, position: Int) {
        val currentItem = productTypes[position]
        holder.binding.apply {
            layoutProductType.setBackgroundColor(Color.parseColor(colorsList[position]))
            tvProductType.text = currentItem.name
            tvProductType.setOnClickListener {
                onItemClickListener?.let {
                    it(currentItem) }
            }
        }
    }

    private var onItemClickListener: ((ProductType) -> Unit)? = null

    fun setOnItemClickListener(listener:(ProductType) -> Unit){
        onItemClickListener = listener
    }

    override fun getItemCount(): Int = productTypes.size

    class ProductTypeViewHolder(val binding: ProductTypeListItemBinding): RecyclerView.ViewHolder(binding.root){
        companion object{
            fun from(parent: ViewGroup): ProductTypeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProductTypeListItemBinding.inflate(layoutInflater, parent, false)
                return ProductTypeViewHolder(binding)
            }
        }
    }

    private fun View.toggleVisibility(){
        visibility = if(this.isVisible){
            View.GONE
        }else{
            View.VISIBLE
        }
    }
}


//val arrayAdapter = ArrayAdapter(context, R.layout.dropdown_item, categories)