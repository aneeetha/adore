package com.example.adore.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.databinding.DataBindingUtil
import com.example.adore.R
import com.example.adore.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val navBar = activity?.findViewById<View>(R.id.bottom_navigation_view)
        navBar?.visibility = View.VISIBLE

        val binding: FragmentCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        binding.apply {

            categoryMen.setOnClickListener {
               // findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToVarietiesFragment("men"))
                findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToProductsFragment())
            }

            categoryWomen.setOnClickListener {
               // findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToVarietiesFragment("women"))
                findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToProductsFragment())
            }

            ivSearchIcon.setOnClickListener {
                findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToSearchFragment())
            }
        }

        return binding.root
    }
}