package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adore.R
import com.example.adore.adapters.GenderBasedProductTypeAdapter
import com.example.adore.databinding.FragmentProductTypeBinding
import com.example.adore.models.enums.Gender
import com.example.adore.models.enums.ProductType

class ProductTypeFragment : Fragment() {

    lateinit var binding: FragmentProductTypeBinding
    lateinit var productTypeAdapter: GenderBasedProductTypeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val navBar = activity?.findViewById<View>(R.id.bottom_navigation_view)
        navBar?.visibility = View.VISIBLE

        binding = FragmentProductTypeBinding.inflate(inflater, container, false)

        val gender = ProductTypeFragmentArgs.fromBundle(requireArguments()).gender

        val colorsList = listOf("#B67171", "#839B97", "#4E3D53", "#BFB051", "#B67171", "#839B97", "#4E3D53", "#BFB051", "#B67171")

        productTypeAdapter = GenderBasedProductTypeAdapter(
            ProductType.values().filter { it.gender == gender || it.gender == Gender.Unisex }.map { it.headingValue }, colorsList)

        binding.rvProductType.apply {
            adapter = productTypeAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        binding.apply {
            ivBackIcon.setOnClickListener {
                Log.e("Navigation", "${findNavController().currentDestination}")
                findNavController().navigateUp()
            }
        }

        productTypeAdapter.setOnItemClickListener { productType ->
            findNavController().navigate(ProductTypeFragmentDirections.actionProductTypeFragmentToProductsFragment(gender, productType))
        }


        return binding.root
    }
}

