package com.example.adore.adapters

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.adore.ui.login.LoginTabFragment
import com.example.adore.ui.login.SignUpTabFragment

private const val NUM_TABS = 2

class LoginViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle){

    override fun getItemCount() = NUM_TABS

    override fun createFragment(position: Int) = when(position){
            0-> LoginTabFragment()
            1 -> SignUpTabFragment()
            else -> LoginTabFragment()
        }

}