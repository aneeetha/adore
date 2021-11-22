package com.example.adore.ui.fragments

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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_product_details.*

class ProductDetailsFragment : Fragment() {

    lateinit var viewModel: ProductDetailsViewModel
    lateinit var binding:FragmentProductDetailsBinding
    lateinit var productSizeAdapter: ProductSizeAdapter
    lateinit var arguments: ProductDetailsFragmentArgs
    lateinit var productsViewModel: ProductsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_product_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val adoreRepository = AdoreRepository(AdoreDatabase(application))

        productsViewModel = (activity as AdorableActivity).viewModel

        arguments = ProductDetailsFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = ProductDetailsViewModelProviderFactory(arguments.product, adoreRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductDetailsViewModel::class.java)
        setupRecyclerView()

        binding.productDetailsViewModel = viewModel
        binding.lifecycleOwner = this

        productSizeAdapter.setOnItemClickListener {
            viewModel.setChosenProductSize(it)
        }

        iv_back_icon.setOnClickListener {
            Log.e("Navigation", "${findNavController().currentDestination}")
            //childFragmentManager.popBackStackImmediate()
            findNavController().navigateUp()
            //parentFragmentManager.popBackStackImmediate()
        }

//        val callback = object: OnBackPressedCallback(true){
//            override fun handleOnBackPressed() {
//                requireActivity().onBackPressed()
//            }
//        }
//
//        binding.ivBackIcon.setOnClickListener {
//            (activity as AdorableActivity).onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
//        }

        viewModel.addedToFavlist.observe(viewLifecycleOwner, {
            if(it==true){
                tv_add_to_favo.setText(R.string.added_to_favo)
            }
        })

        viewModel.addToCartClicked.observe(viewLifecycleOwner, {
            if(it==true){
                btn_add_to_cart.text = context?.getString(R.string.go_to_cart)
            }else{
                btn_add_to_cart.text = context?.getString(R.string.add_to_cart)
            }
        })

        viewModel.navigateToCartFragment.observe(viewLifecycleOwner, Observer {
            if(it==true){
                findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToCartFragment())
                viewModel.setNavigatedToCartFragment()
            }
        })

        viewModel.navigateToFavoFragment.observe(viewLifecycleOwner, {
            if(it==true){
                findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToFavoFragment())
                viewModel.setNavigatedToFavoFragment()
            }
        })

        viewModel.navigateToSearchFragment.observe(viewLifecycleOwner, {
            if(it==true){
                findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToSearchFragment())
                viewModel.setNavigatedToSearchFragment()
            }
        })

        viewModel.showSnackBar.observe(viewLifecycleOwner, Observer {
            if(it==true){
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.size_not_chosen_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()

                viewModel.doneShowingSnackBar()
            }
        })
    }

    private fun setupRecyclerView(){
        productSizeAdapter = ProductSizeAdapter()
        productSizeAdapter.submitList(arguments.product.stock)
        rv_product_size.apply {
            adapter = productSizeAdapter
            layoutManager = GridLayoutManager(activity, 5)
        }
    }
}