package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adore.R
import com.example.adore.adapters.ProductsAdapter
import com.example.adore.databinding.FragmentSearchBinding
import com.example.adore.ui.AdorableActivity
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.util.Constants.Companion.SEARCH_TIME_DELAY
import com.example.adore.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.*

class SearchFragment : Fragment() {

    private lateinit var viewModel: ProductsViewModel
    private lateinit var productsAdapter: ProductsAdapter
    lateinit var binding: FragmentSearchBinding

    var job: Job? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        viewModel = (activity as AdorableActivity).viewModel
        setUpRecyclerView()

        productsAdapter.setOnItemClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToProductDetailsFragment(it))
        }


        binding.apply {
            etSearchBar.addTextChangedListener { editable ->
                job?.cancel()
                job = MainScope().launch {
                    delay(SEARCH_TIME_DELAY)
                    editable?.let {
                        if (editable.toString().isNotEmpty()) {
                            viewModel.getSearchResult(editable.toString())
                        }
                    }
                }
            }
            ivBackIcon.setOnClickListener {
                requireActivity().onBackPressed()
               // findNavController().navigateUp()
            }
        }

        viewModel.apply {
            searchResult.observe(viewLifecycleOwner, {
                it?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            hideNoResultFound()
                            response.data?.let { productsResponse ->
                                productsAdapter.differ.submitList(productsResponse.products)
                                doneShowingSearchResults()
                            }
                        }
                        is Resource.Empty -> showNoResultFound()
                        is Resource.Error -> {
                            hideProgressBar()
                            response.message?.let { message ->
                                showSnackBarWithMessage(message)
                                Log.e("SearchFragment", "An error occurred: $message")
                            }
                        }
                        is Resource.Loading -> {
                            showProgressBar()
                        }
                    }
                }

            })
        }

        return binding.root
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        hideNoResultFound()
        binding.apply {
            progressBar.visibility = View.VISIBLE
            rvSearch.visibility = View.INVISIBLE
        }
    }

    private fun showNoResultFound(){
        hideProgressBar()
        binding.apply {
            rvSearch.visibility = View.INVISIBLE
            imgNotFound.visibility = View.VISIBLE
        }
    }

    private fun hideNoResultFound(){
        binding.apply {
            rvSearch.visibility = View.VISIBLE
            imgNotFound.visibility = View.GONE
        }
    }

    private fun setUpRecyclerView(){
        productsAdapter = ProductsAdapter()
        binding.rvSearch.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    private fun showSnackBarWithMessage(message: String){
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

}