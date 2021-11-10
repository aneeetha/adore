package com.example.adore.ui.products

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adore.R
import com.example.adore.adapters.ProductsAdapter
import com.example.adore.ui.AdorableActivity
import com.example.adore.ui.ProductsViewModel
import com.example.adore.util.Resource
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : Fragment(R.layout.fragment_products) {

    lateinit var viewModel: ProductsViewModel
    lateinit var productsAdapter: ProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as AdorableActivity).viewModel
        setUpRecyclerView()

        viewModel.allProducts.observe(viewLifecycleOwner, { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { productsResponse ->
                        productsAdapter.differ.submitList(productsResponse.products)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("ProductsFragment", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })
    }

    private fun hideProgressBar(){
        progress_bar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        progress_bar.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView(){
        productsAdapter = ProductsAdapter()
        rv_products.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }
}