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
import com.example.adore.databsae.ProductDatabase
import com.example.adore.ui.viewmodels.ProductDetailsViewModel
import com.example.adore.ui.viewmodels.factory.ProductDetailsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_product_details.*

class ProductDetailsFragment : Fragment() {

    lateinit var viewModel: ProductDetailsViewModel
    lateinit var binding:FragmentProductDetailsBinding
    lateinit var productSizAdapter: ProductSizeAdapter
    lateinit var arguments: ProductDetailsFragmentArgs

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
        val dataSource = ProductDatabase(application).getProductDatabaseDao()

        arguments = ProductDetailsFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = ProductDetailsViewModelProviderFactory(arguments.product, dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductDetailsViewModel::class.java)
        setupRecyclerView()

        binding.productDetailsViewModel = viewModel
        binding.lifecycleOwner = this

        productSizAdapter.setOnItemClickListener {
            viewModel.setChosenProductSize(it)
        }

        viewModel.navigateToCart.observe(viewLifecycleOwner, Observer {
            if(it==true){
                findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToCartFragment())
                viewModel.setNavigatedToCart()
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
        productSizAdapter = ProductSizeAdapter(arguments.product.inStockCount)
        rv_product_size.apply {
            adapter = productSizAdapter
            layoutManager = GridLayoutManager(activity, 5)
        }
    }
}