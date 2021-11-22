package com.example.adore.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.adore.R
import com.example.adore.adapters.LoginViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(R.layout.fragment_login) {

      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginTabs = arrayOf("Login", "Signup")
        login_view_pager.adapter = LoginViewPagerAdapter(parentFragmentManager, lifecycle)
        TabLayoutMediator(login_tab_layout, login_view_pager) { tab, position ->
            tab.text = loginTabs[position]
        }.attach()
    }

}