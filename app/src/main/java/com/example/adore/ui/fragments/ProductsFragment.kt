package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adore.R
import com.example.adore.adapters.ProductsAdapter
import com.example.adore.databinding.FragmentProductsBinding
import com.example.adore.models.enums.Category
import com.example.adore.models.enums.Gender
import com.example.adore.ui.AdorableActivity
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.util.Resource
import com.google.android.material.snackbar.Snackbar

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

        val arguments = ProductsFragmentArgs.fromBundle(requireArguments())
        viewModel.getProductsOfType(arguments.gender, arguments.productType)


        val categories= Category.values().filter { (it.gender == arguments.gender || it.gender== Gender.Unisex) && it.type.contains(arguments.productType) }.map { it.headingValue }.toMutableList()
        categories.add(0, "None")
        val categoriesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)

        productsAdapter.setOnItemClickListener {
            findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(it))
        }


        binding.apply {
            ivBackIcon.setOnClickListener {
                findNavController().navigateUp()
                //requireActivity().onBackPressed()
                //findNavController().backStack.pop()
//            childFragmentManager.popBackStackImmediate()
                //parentFragmentManager.popBackStackImmediate()
            }

            ivCartIcon.setOnClickListener {
                findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToCartFragment())
            }

            ivFavoIcon.setOnClickListener {
                findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToFavoFragment())
            }

            ivSearchIcon.setOnClickListener {
                findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToSearchFragment())
            }

            (tilCategories.editText as AutoCompleteTextView).setAdapter(categoriesAdapter)
            autoTvCategories.setOnItemClickListener { adapterView, _, i, _ ->
                val selectedCategory = adapterView.getItemAtPosition(i).toString()
                Log.e("ProductsFragment", selectedCategory)
                if(selectedCategory!="None") {
                    viewModel.getProductWithCategories(
                        arguments.gender,
                        arguments.productType,
                        Category.values().first{it.headingValue==selectedCategory}
                    )
                }else{
                    viewModel.getProductsOfType(arguments.gender, arguments.productType)
                }
            }
        }

        viewModel.apply {
            productsOfType.observe(viewLifecycleOwner, { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { productsResponse ->
                            productsAdapter.differ.submitList(productsResponse.products)
                        }
                    }
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

            productsOfCategory.observe(viewLifecycleOwner, { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { productsResponse ->
                            productsAdapter.differ.submitList(productsResponse.products)
                        }
                    }
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


    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView() {
        productsAdapter = ProductsAdapter()
        binding.rvProducts.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    private fun showSnackBarWithMessage(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}