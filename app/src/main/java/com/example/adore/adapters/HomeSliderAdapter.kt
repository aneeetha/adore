package com.example.adore.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.adore.databinding.HomeSlideContainerBinding
import com.example.adore.models.dataClasses.Product
import com.example.adore.util.HomeSliderItem
import kotlinx.android.synthetic.main.home_slide_container.view.*

class HomeSliderAdapter(private val viewPager: ViewPager2):
    RecyclerView.Adapter<HomeSliderAdapter.SliderViewHolder>() {

    private val differCallback = object: DiffUtil.ItemCallback<HomeSliderItem>(){
        override fun areItemsTheSame(
            oldItem: HomeSliderItem,
            newItem: HomeSliderItem
        ) = oldItem.product == newItem.product

        override fun areContentsTheSame(oldItem: HomeSliderItem, newItem: HomeSliderItem) = oldItem==newItem
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SliderViewHolder.from(parent)

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.itemView.apply {
           Glide.with(this).load(currentItem.product.imageUrl).into(iv_image_slide)
            iv_image_slide.setOnClickListener{
                onItemClickListener?.let { it(currentItem.product) }
            }
        }
        if(position == differ.currentList.size-2)
            viewPager.post(runnable)
    }

    private var onItemClickListener: ((Product) -> Unit)? = null

    fun setOnItemClickListener(listener:(Product) -> Unit){
        onItemClickListener = listener
    }


    class SliderViewHolder(val binding: HomeSlideContainerBinding): RecyclerView.ViewHolder(binding.root){
        companion object{
            fun from(parent: ViewGroup): SliderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HomeSlideContainerBinding.inflate(layoutInflater, parent, false)
                return SliderViewHolder(binding)
            }
        }
    }


    private val runnable = Runnable {
        val newList = differ.currentList
        differ.submitList(newList.shuffled())
    }

    override fun getItemCount()=differ.currentList.size


}