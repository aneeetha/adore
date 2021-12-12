package com.example.adore.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adore.R
import com.example.adore.adapters.NoFilterArrayAdapter
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

    private var selectedCategory: String? = null

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

        val categories = Category.values().filter {
            (it.gender == arguments.gender || it.gender == Gender.Unisex) && it.type.contains(
                arguments.productType
            )
        }.map { it.headingValue }.toMutableList()
        categories.add(0, "None")
        val categoriesAdapter =
            NoFilterArrayAdapter(requireContext(), R.layout.dropdown_item, categories)

        productsAdapter.setOnItemClickListener {
            findNavController().navigate(
                ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(
                    it
                )
            )
        }

        binding.apply {

            viewModel.getProducts(
                arguments.gender,
                arguments.productType,
                Category.values().firstOrNull { it.headingValue == selectedCategory })

            ivBackIcon.setOnClickListener {
                findNavController().navigateUp()
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

            (autoTvCategories).setAdapter(categoriesAdapter)

            autoTvCategories.setOnItemClickListener { adapterView, _, i, _ ->
                (adapterView.getItemAtPosition(i).toString()).let { value->
                    if (value != selectedCategory)
                        selectedCategory = value
                        viewModel.getProducts(
                            arguments.gender,
                            arguments.productType,
                            Category.values().firstOrNull { it.headingValue == selectedCategory })
                }
            }
        }

        viewModel.apply {
            productsResult.observe(viewLifecycleOwner, {
                it?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            hideNoResultFound()
                            response.data?.let { productsResponse ->
                                productsAdapter.differ.submitList(productsResponse.products)
                                doneShowingProductsResult()
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
                }
            })
        }

        return binding.root
    }


    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            imgNotFound.visibility = View.GONE
        }
    }

    private fun showNoResultFound() {
        hideProgressBar()
        binding.apply {
            rvProducts.visibility = View.INVISIBLE
            imgNotFound.visibility = View.VISIBLE
        }
    }

    private fun hideNoResultFound() {
        binding.apply {
            rvProducts.visibility = View.VISIBLE
            imgNotFound.visibility = View.GONE
        }
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