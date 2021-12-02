package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adore.R
import com.example.adore.adapters.CartAdapter
import com.example.adore.databinding.FragmentCartBinding
import com.example.adore.models.entities.Address
import com.example.adore.ui.AdorableActivity
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.util.AdoreLogic
import com.example.adore.util.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


class CartFragment : Fragment() {

    lateinit var viewModel: ProductsViewModel
    lateinit var cartAdapter: CartAdapter
    lateinit var binding: FragmentCartBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)

        val navBar = activity?.findViewById<View>(R.id.bottom_navigation_view)
        navBar?.visibility = View.VISIBLE

        viewModel = (activity as AdorableActivity).viewModel
        setUpRecyclerView()
        viewModel.getCartItem()


        viewModel.apply {
            cartItems.observe(viewLifecycleOwner, { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { cartResponse ->
                            cartAdapter.submitList(cartResponse.cartItems)
                            getTotalPrice(cartResponse.cartItems)
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        hideViewsAndActions()
                        response.message?.let { message ->
                            showSnackBarWithMessage(message)
                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }

            })

            cartSnackBarMessage.observe(viewLifecycleOwner, { response ->
                response?.data?.let {
                    showSnackBarWithMessage(it.message)
                    doneShowingSnackBarInCart()
                }
            })

            navigateToOrderSuccessPage.observe(viewLifecycleOwner, {
                it?.let {
                    findNavController().navigate(CartFragmentDirections.actionCartFragmentToOrderSuccessFragment())
                    doneNavigatingToOrderSuccessPage()
                }
            })
        }

        binding.apply {

            viewModel.totalPrice.observe(viewLifecycleOwner, {
                val deliveryCharge = AdoreLogic.getDeliveryCharge(it)

                context?.getString(R.string.delivery_charge, deliveryCharge.toString())
                    ?.let { text ->
                        tvDeliveryCharge.text = HtmlCompat.fromHtml(
                            String.format(text, "placeholder1"),
                            HtmlCompat.FROM_HTML_MODE_COMPACT
                        )
                    }

                context?.getString(R.string.total_price, it.plus(deliveryCharge).toString())
                    ?.let { text ->
                        tvTotalPrice.text = HtmlCompat.fromHtml(
                            String.format(text, "placeholder1"),
                            HtmlCompat.FROM_HTML_MODE_COMPACT
                        )
                    }
            })

            btnCheckout.setOnClickListener {
                getAddress()
            }
        }


        return binding.root
    }


    private fun hideProgressBar() {
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            showViewsAndActions()
        }
    }

    private fun showProgressBar() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            hideViewsAndActions()
        }
    }

    private fun hideViewsAndActions() {
        binding.apply {
            btnCheckout.visibility = View.INVISIBLE
            tvTotalPrice.visibility = View.INVISIBLE
            rvCart.visibility = View.INVISIBLE
            tvDeliveryCharge.visibility = View.INVISIBLE
        }
    }

    private fun showViewsAndActions() {
        binding.apply {
            btnCheckout.visibility = View.VISIBLE
            tvTotalPrice.visibility = View.VISIBLE
            rvCart.visibility = View.VISIBLE
            tvDeliveryCharge.visibility = View.VISIBLE
        }
    }

    private fun setUpRecyclerView() {
        cartAdapter = CartAdapter(viewModel)
        binding.rvCart.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun getAddress() {
        viewModel.currentUser.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { currentUserResponse ->
                        if (currentUserResponse.userId != 0L) {
                            viewModel.getAddressesOfCurrentUser(currentUserResponse.userId)
                                .observe(viewLifecycleOwner, {
                                    it?.let {
                                        val addressList = it.addresses.map { address ->
                                            address.addressType
                                        }
                                        showConfirmationDialog(
                                            addressList.toTypedArray(),
                                            it.addresses
                                        )
                                    }
                                })
                        }
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

    private fun showConfirmationDialog(addressTypes: Array<String>, address: List<Address>): Int {
        var selectedAddressIndex = 0
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Select Delivery Address")
            .setSingleChoiceItems(addressTypes, selectedAddressIndex) { _, which ->
                selectedAddressIndex = which
            }
            .setNeutralButton("Add New") { _, _ ->
                findNavController().navigate(CartFragmentDirections.actionCartFragmentToAddressFragment())
            }
        dialog.show()
        if (addressTypes.isNotEmpty()) {
            dialog.setPositiveButton("Confirm") { _, _ ->
                showSnackBarWithMessage("Address chosen!")
                viewModel.placeOrder(address[selectedAddressIndex].addressId!!)
            }
        }
        return selectedAddressIndex
    }

    private fun showSnackBarWithMessage(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}