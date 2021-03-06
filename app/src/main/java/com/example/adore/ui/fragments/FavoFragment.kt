package com.example.adore.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adore.R
import com.example.adore.adapters.FavoProductAdapter
import com.example.adore.databinding.FragmentFavoBinding
import com.example.adore.ui.AdorableActivity
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.util.Resource
import com.google.android.material.snackbar.Snackbar

class FavoFragment : Fragment() {

    lateinit var viewModel: ProductsViewModel
    lateinit var favoProductAdapter: FavoProductAdapter
    lateinit var binding: FragmentFavoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoBinding.inflate(inflater, container, false)


        val navBar = activity?.findViewById<View>(R.id.bottom_navigation_view)
        navBar?.visibility = View.VISIBLE


        viewModel = (activity as AdorableActivity).viewModel
        setUpRecyclerView()
        viewModel.getFavlist()

        favoProductAdapter.setOnItemClickListener {
            findNavController().navigate(FavoFragmentDirections.actionFavoFragmentToProductDetailsFragment(it))
        }

        viewModel.apply {
            favoToastMessage.observe(viewLifecycleOwner, { response ->
                response?.data?.let {
                    showSnackBarWithMessage(it.message)
                    doneShowingSnackBarInFavo()
                }
            })

            favlistResult.observe(viewLifecycleOwner, { response ->
                when (response) {
                    is Resource.Success -> {
                        showRecyclerView()
                        response.data?.let { productsResponse ->
                            favoProductAdapter.submitList(productsResponse.products)
                        }
                    }
                    is Resource.Empty -> showNoResultFound()

                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let { message ->
                            showSnackBarWithMessage(message)
                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })
        }

        return binding.root
    }

    private fun showRecyclerView() {
        hideProgressBar()
        hideNoResultFound()
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView() {
        favoProductAdapter = FavoProductAdapter(viewModel)
        binding.rvFavoProducts.apply {
            adapter = favoProductAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    private fun showSnackBarWithMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showNoResultFound(){
        hideProgressBar()
        binding.apply {
            rvFavoProducts.visibility = View.INVISIBLE
            imgNotFound.visibility = View.VISIBLE
        }
    }

    private fun hideNoResultFound(){
        binding.apply {
            rvFavoProducts.visibility = View.VISIBLE
            imgNotFound.visibility = View.GONE
        }
    }

}
