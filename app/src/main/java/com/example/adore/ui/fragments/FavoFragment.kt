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
import kotlinx.android.synthetic.main.fragment_favo.*
import kotlinx.android.synthetic.main.fragment_favo.progress_bar

class FavoFragment : Fragment(R.layout.fragment_favo) {

    lateinit var viewModel: ProductsViewModel
    lateinit var productsAdapter: ProductsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as AdorableActivity).viewModel
        setUpRecyclerView()
        viewModel.getFavlist()

        productsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("product", it)
            }
            findNavController().navigate(
                R.id.action_favoFragment_to_productDetailsFragment,
                bundle
            )
        }

        iv_search_icon.setOnClickListener {
            findNavController().navigate(FavoFragmentDirections.actionFavoFragmentToSearchFragment())
        }

        viewModel.favlistResult.observe(viewLifecycleOwner, { response ->
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
                        Log.e("SearchFragment", "An error occurred: $message")
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

    private fun setUpRecyclerView() {
        productsAdapter = ProductsAdapter()
        rv_favo_products.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }
}
