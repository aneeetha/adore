package com.example.adore.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adore.R
import com.example.adore.databinding.GridItemBinding

class VarietiesAdapter(val context: Context, private val varieties: List<String>, private val varietyImages: List<Int>): RecyclerView.Adapter<VarietiesAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
        return CategoryViewHolder(view, parent)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.apply{
            imageView.setImageResource(varietyImages[position])
            textView.text = varieties[position]
        }
    }

    override fun getItemCount() = varieties.size

    inner class CategoryViewHolder(view: View, parent: ViewGroup): RecyclerView.ViewHolder(view){

        val binding = GridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val imageView= binding.ivCategoryImage
        val textView= binding.tvCategory
    }
}