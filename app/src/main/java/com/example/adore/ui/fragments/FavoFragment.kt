package com.example.adore.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.adore.R
import com.example.adore.adapters.ProductsAdapter
import com.example.adore.ui.AdorableActivity
import com.example.adore.ui.viewmodels.ProductsViewModel

class FavoFragment : Fragment(R.layout.fragment_favo) {

    lateinit var viewModel: ProductsViewModel
    lateinit var productsAdapter: ProductsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as AdorableActivity).viewModel

//        productsAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putSerializable("product", it)
//            }
//            findNavController().navigate(
//                R.id.action_searchFragment_to_productDetailsFragment,
//                bundle
//            )
//        }
    }

}