package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adore.R
import com.example.adore.adapters.ProductsAdapter
import com.example.adore.ui.AdorableActivity
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.util.Resource
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : Fragment(R.layout.fragment_products) {

    lateinit var viewModel: ProductsViewModel
    lateinit var productsAdapter: ProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navBar = activity?.findViewById<View>(R.id.bottom_navigation_view)
        navBar?.visibility = View.GONE

        viewModel = (activity as AdorableActivity).viewModel
        setUpRecyclerView()

//        viewModel.navigateToProductDetails.observe(viewLifecycleOwner, {id ->
//            id?.let {
//                findNavController().navigate(
//                    ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(it)
//                )
//                viewModel.onProductDetailsNavigated()
//
//                android:onClick="@{() -> clickListener.onClick(product)}"
//            }
//        })

        productsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("product", it)
            }
            findNavController().navigate(
                R.id.action_productsFragment_to_productDetailsFragment,
                bundle
            )
        }

        viewModel.allProducts.observe(viewLifecycleOwner, { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { productsResponse ->
                        productsAdapter.differ.submitList(productsResponse.products)
                        //Log.i("ProductsFragment", productsResponse.products.toString())
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
        productsAdapter = ProductsAdapter(ProductsAdapter.ProductClickListener {
            viewModel.onProductClicked(it)
        })
        rv_products.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }
}