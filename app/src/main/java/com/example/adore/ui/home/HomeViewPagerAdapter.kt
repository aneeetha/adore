package com.example.adore.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.adore.ui.category.CategoryFragment

private const val NUM_TABS = 2

class HomeViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment =  when(position){
            0 -> CategoryFragment()
            else -> CategoryFragment()
        }
}