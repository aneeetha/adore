package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adore.R
import com.example.adore.adapters.ProductsAdapter
import com.example.adore.databinding.FragmentProductsBinding
import com.example.adore.ui.AdorableActivity
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_product_details.*
import kotlinx.android.synthetic.main.fragment_products.*
import kotlinx.android.synthetic.main.fragment_products.iv_back_icon
import kotlinx.android.synthetic.main.fragment_products.iv_cart_icon
import kotlinx.android.synthetic.main.fragment_products.iv_favo_icon
import kotlinx.android.synthetic.main.fragment_products.iv_search_icon

class ProductsFragment : Fragment() {

    lateinit var viewModel: ProductsViewModel
    lateinit var productsAdapter: ProductsAdapter
    lateinit var binding: FragmentProductsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater, container, false)


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
            Log.e("Navigation", "${findNavController().currentDestination}")
            findNavController().navigate(
                R.id.action_productsFragment_to_productDetailsFragment,
                bundle
            )
        }

//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                val a =childFragmentManager.getBackStackEntryAt(1)
//                Log.e("Navigation", "$a ...")
//                Log.e("Navigation", "here")
//            }
//        }

        binding.ivBackIcon.setOnClickListener {
            Log.e("Navigation", "${findNavController().currentDestination}")
            findNavController().navigateUp()
            //requireActivity().onBackPressed()
            //findNavController().backStack.pop()
//            childFragmentManager.popBackStackImmediate()
            //parentFragmentManager.popBackStackImmediate()
        }

        binding.ivCartIcon.setOnClickListener {
            findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToCartFragment())
        }

        binding.ivFavoIcon.setOnClickListener {
            findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToFavoFragment())
        }

        binding.ivSearchIcon.setOnClickListener {
            findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToSearchFragment())
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
                        showSnackBarWithMessage(message)
                        Log.e("ProductsFragment", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })

        return binding.root
    }


    private fun hideProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView(){
        productsAdapter = ProductsAdapter()
        binding.rvProducts.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    private fun showSnackBarWithMessage(message: String){
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT // How long to display the message.
        ).show()
    }
}