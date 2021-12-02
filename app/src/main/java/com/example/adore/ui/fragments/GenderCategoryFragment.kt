package com.example.adore.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.adore.R
import com.example.adore.databinding.FragmentGenderCategoryBinding
import com.example.adore.models.enums.Gender


class GenderCategoryFragment : Fragment() {


    lateinit var binding: FragmentGenderCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val navBar = activity?.findViewById<View>(R.id.bottom_navigation_view)
        navBar?.visibility = View.VISIBLE

        binding = FragmentGenderCategoryBinding.inflate(inflater, container, false)


        binding.apply {
            categoryMen.setOnClickListener {
                findNavController().navigate(GenderCategoryFragmentDirections.actionGenderCategoryFragmentToProductTypeFragment(Gender.Men))
            }

            categoryWomen.setOnClickListener {
                findNavController().navigate(GenderCategoryFragmentDirections.actionGenderCategoryFragmentToProductTypeFragment(Gender.Women))
            }

            ivSearchIcon.setOnClickListener {
                findNavController().navigate(GenderCategoryFragmentDirections.actionGenderCategoryFragmentToSearchFragment())
            }
        }

        return binding.root
    }


}