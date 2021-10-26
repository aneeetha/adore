package com.example.adore.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.adore.R

class CollectionHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_collections_home, container, false)
        val viewPager: ViewPager = view.findViewById(R.id.slider_view_pager)
        val sliderAdapter = VarietiesSliderAdapter(requireContext())
        viewPager.adapter = sliderAdapter
        return view
    }

}