package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adore.R
import com.example.adore.adapters.ProductsAdapter
import com.example.adore.ui.AdorableActivity
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.util.Constants.Companion.SEARCH_TIME_DELAY
import com.example.adore.util.Resource
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.*

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var viewModel: ProductsViewModel
    private lateinit var productsAdapter: ProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as AdorableActivity).viewModel
        setUpRecyclerView()

//        viewModel.navigateToProductDetails.observe(viewLifecycleOwner, {id ->
//            id?.let {
//                findNavController().navigate(
//                    ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(it)
//                )
//               // viewModel.onProductDetailsNavigated()
//            }
//        })

        productsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("product", it)
            }
            findNavController().navigate(
                R.id.action_searchFragment_to_productDetailsFragment,
                bundle
            )
        }

        var job: Job? = null

        et_search_bar.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        viewModel.getSearchResult(editable.toString())
                    }
                }
            }
        }

        viewModel.searchResult.observe(viewLifecycleOwner, { response ->
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

    private fun setUpRecyclerView(){
        productsAdapter = ProductsAdapter()
        rv_search.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

}