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
import com.example.adore.models.enums.CustomLabel
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.AdorableActivity
import com.example.adore.ui.viewmodels.ProductDetailsViewModel
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.ui.viewmodels.factory.ProductDetailsViewModelProviderFactory
import com.example.adore.util.AdoreLogic
import com.example.adore.util.Constants
import com.google.android.material.snackbar.Snackbar

class ProductDetailsFragment : Fragment() {

    lateinit var viewModel: ProductDetailsViewModel
    lateinit var binding: FragmentProductDetailsBinding
    lateinit var productSizeAdapter: ProductSizeAdapter
    lateinit var arguments: ProductDetailsFragmentArgs
    lateinit var productsViewModel: ProductsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_product_details, container, false
        )

        val application = requireNotNull(this.activity).application
        val adoreRepository = AdoreRepository(AdoreDatabase(application))

        productsViewModel = (activity as AdorableActivity).viewModel

        arguments = ProductDetailsFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory =
            ProductDetailsViewModelProviderFactory(arguments.product, adoreRepository)

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ProductDetailsViewModel::class.java)

        setupRecyclerView()

        binding.productDetailsViewModel = viewModel
        binding.lifecycleOwner = this

        val discount = AdoreLogic.getDiscount(arguments.product.customLabels)
        val sellingPrice = Constants.CURRENCY + (arguments.product.price - (arguments.product.price.times(discount).div(100))).toString()


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
                //childFragmentManager.popBackStackImmediate()
                findNavController().navigateUp()
                //parentFragmentManager.popBackStackImmediate()
            }
        }



        viewModel.apply {

            addedToFavlist.observe(viewLifecycleOwner, {
                it?.let{
                    binding.tvAddToFavo.setText(R.string.added_to_favo)
                    doneAddingItemToFavo()
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
}