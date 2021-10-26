package com.example.adore.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.example.adore.R
import com.example.adore.databinding.VarietiesSlideLayoutBinding

class VarietiesSliderAdapter(val context: Context):PagerAdapter() {

    private val headings = arrayOf(
        R.string.festive_collections,
        R.string.ethnic_wear,
        R.string.formal_wear,
        R.string.casuals
    )

    private val images = arrayOf(
        R.drawable.women_ethnic_wear,
        R.drawable.men_ethic_wears,
        R.drawable.formal_wear,
        R.drawable.women_casuals
    )

    override fun getCount() = headings.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = (view === `object`)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layoutInflater = LayoutInflater.from(context)

        val binding = DataBindingUtil.inflate<VarietiesSlideLayoutBinding>(layoutInflater, R.layout.varieties_slide_layout, container, false)
        binding.apply {
            sliderImage.setImageResource(images[position])
            sliderHeading.setText(headings[position])
        }
        container.addView(binding.root)

        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}