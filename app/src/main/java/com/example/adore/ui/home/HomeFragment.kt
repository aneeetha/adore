package com.example.adore.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.example.adore.R
import com.example.adore.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private val homeTabsText = arrayOf("Home", "Categories")
    val homeTabsIcon = arrayOf(R.drawable.general_home_icon, R.drawable.general_category_icon)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =DataBindingUtil.inflate<FragmentHomeBinding>(inflater, R.layout.fragment_home, container, false)
        binding.apply {
            homeViewPager.adapter = HomeViewPagerAdapter(parentFragmentManager, lifecycle)

            TabLayoutMediator(homeTabLayout, homeViewPager){tab, position ->
                tab.icon = ResourcesCompat.getDrawable(resources, homeTabsIcon[position], null)
                tab.text = homeTabsText[position]
            }.attach()


        }

        return binding.root
    }
}