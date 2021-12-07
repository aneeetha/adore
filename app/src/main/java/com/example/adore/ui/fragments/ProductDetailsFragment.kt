package com.example.adore.ui.fragments

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adore.R
import com.example.adore.adapters.ProductSizeAdapter
import com.example.adore.databinding.FragmentProductDetailsBinding
import com.example.adore.databsae.AdoreDatabase
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.AdorableActivity
import com.example.adore.ui.viewmodels.ProductDetailsViewModel
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.ui.viewmodels.factory.ProductDetailsViewModelProviderFactory
import com.example.adore.util.AdoreLogic
import com.example.adore.util.Constants
import com.example.adore.util.Resource
import com.google.android.material.snackbar.Snackbar

class ProductDetailsFragment : Fragment() {

    lateinit var viewModel: ProductDetailsViewModel
    lateinit var binding: FragmentProductDetailsBinding
    lateinit var productSizeAdapter: ProductSizeAdapter
    lateinit var arguments: ProductDetailsFragmentArgs
    lateinit var productsViewModel: ProductsViewModel

    private var itemFavoured = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_product_details, container, false
        )

        val navBar = activity?.findViewById<View>(R.id.bottom_navigation_view)
        navBar?.visibility = View.GONE

        val application = requireNotNull(this.activity).application
        val adoreRepository = AdoreRepository(AdoreDatabase(application))
        productsViewModel = (activity as AdorableActivity).viewModel

        arguments = ProductDetailsFragmentArgs.fromBundle(requireArguments())
        val discount = AdoreLogic.getDiscount(arguments.product.customLabels)
        val sellingPrice = Constants.CURRENCY + (arguments.product.price - (arguments.product.price.times(discount).div(100))).toString()

        val viewModelFactory =
            ProductDetailsViewModelProviderFactory(application, arguments.product, adoreRepository)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ProductDetailsViewModel::class.java)
        setupRecyclerView()

        binding.productDetailsViewModel = viewModel
        binding.lifecycleOwner = this


        productSizeAdapter.setOnItemClickListener {
            viewModel.setChosenProductSize(it)
        }

        binding.apply {
            val discountString = "$discount% discount"
            tvProductPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            tvProductPrice.typeface = Typeface.DEFAULT
            tvProductPrice.textSize = 14F
            tvProductPriceDiscounted.text = sellingPrice
            tvProductDiscount.text = discountString
            ivBackIcon.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        viewModel.apply {
            favoButtonClicked.observe(viewLifecycleOwner, {
                it?.let{
                    when(itemFavoured){
                        false ->{
                            itemFavoured = true
                            toggleFavoButton()
                            addToFavlist()
                        }
                        true ->{
                            itemFavoured = false
                            toggleFavoButton()
                            removeFavoItem()
                        }
                    }
                }
            })

            addToCartClicked.observe(viewLifecycleOwner, {
                if (it == true) {
                    binding.btnAddToCart.text = context?.getString(R.string.go_to_cart)
                } else {
                    binding.btnAddToCart.text = context?.getString(R.string.add_to_cart)
                }
            })

            navigateToCartFragment.observe(viewLifecycleOwner, Observer {
                if (it == true) {
                    findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToCartFragment())
                    setNavigatedToCartFragment()
                }
            })

            navigateToFavoFragment.observe(viewLifecycleOwner, {
                if (it == true) {
                    findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToFavoFragment())
                    setNavigatedToFavoFragment()
                }
            })

            navigateToSearchFragment.observe(viewLifecycleOwner, {
                if (it == true) {
                    findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToSearchFragment())
                    setNavigatedToSearchFragment()
                }
            })

            snackBarMessage.observe(viewLifecycleOwner, { response ->
                response?.data?.let { data ->
                    showSnackBarMessage(data.message)
                    doneShowingSnackBarWithMessage()
                }
            })

            viewModel.showSnackBar.observe(viewLifecycleOwner, Observer {
                it?.let{
                    showSnackBarMessage(getString(R.string.size_not_chosen_message))
                    doneShowingSnackBar()
                }
            })

            favlistResult.observe(viewLifecycleOwner, { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { productsResponse ->
                            itemFavoured = productsResponse.products.contains(arguments.product)
                            Log.e("ProductDetailsFragment", "$itemFavoured")
                            if(itemFavoured) toggleFavoButton()
                        }
                    }
                    is Resource.Empty -> hideProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let { message ->
                            showSnackBarMessage(message)
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

    private fun setupRecyclerView() {
        productSizeAdapter = ProductSizeAdapter()
        productSizeAdapter.submitList(arguments.product.stock)
        binding.rvProductSize.apply {
            adapter = productSizeAdapter
            layoutManager = GridLayoutManager(activity, 5)
        }
    }

    private fun showSnackBarMessage(message: String){
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun hideProgressBar() {
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            svProductDetails.visibility = View.VISIBLE
            actionButtonsLayout.visibility = View.VISIBLE
        }
    }

    private fun showProgressBar() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            svProductDetails.visibility = View.INVISIBLE
            actionButtonsLayout.visibility = View.INVISIBLE
        }
    }

    private fun toggleFavoButton(){
        binding.imgBtnAddToFavo.setImageResource(
            when(itemFavoured){
                true -> {
                    R.drawable.favo_filled_red_heart
                }
                false -> {
                    R.drawable.favo_empty_heart
                }
            }
        )
    }
}