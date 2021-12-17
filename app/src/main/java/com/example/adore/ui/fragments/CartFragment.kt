package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
                        showViewsAndActions()
                        response.data?.let { cartResponse ->
                            cartAdapter.submitList(cartResponse.cartItems)
                            getTotalPriceInCart(cartResponse.cartItems)
                        }
                    }
                    is Resource.Empty -> {
                        Log.e("ViewModel", "empty")
                        showNoResultFound()
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        hideViewsAndActions()
                        response.message?.let { message ->
                            showToast(message)
                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }

            })

            cartToastMessage.observe(viewLifecycleOwner, { response ->
                response?.data?.let {
                    showToast(it.message)
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

    private fun getAddress() {
        viewModel.apply {
            getAddressesOfCurrentUser().observe(viewLifecycleOwner, {
                it?.let {
                    it.addresses.map { address ->
                        address.addressType
                    }.apply {
                        if (size > 0) {
                            showConfirmationDialog(
                                this.toTypedArray(),
                                it.addresses
                            )
                        } else {
                            showDialogToAddNewAddress()
                        }
                    }
                }
            })
        }
    }

    private fun showNoResultFound() {
        hideProgressBar()
        hideViewsAndActions()
        binding.apply {
            imgNotFound.visibility = View.VISIBLE
        }
    }

    private fun hideNoResultFound() {
        binding.imgNotFound.visibility = View.GONE
    }


    private fun hideProgressBar() {
        binding.apply {
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun showProgressBar() {
        hideNoResultFound()
        binding.apply {
            progressBar.visibility = View.VISIBLE
            tvTotalPrice.visibility = View.INVISIBLE
            tvDeliveryCharge.visibility = View.INVISIBLE

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
        hideProgressBar()
        hideNoResultFound()
    }

    private fun setUpRecyclerView() {
        cartAdapter = CartAdapter(viewModel)
        binding.rvCart.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun showConfirmationDialog(addressTypes: Array<String>, address: List<Address>){
        var selectedAddressIndex = 0
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Select Delivery Address")
            .setSingleChoiceItems(addressTypes, selectedAddressIndex) { _, which ->
                Log.e("CartFragment", "$which")
                selectedAddressIndex = which
            }
            .setNeutralButton("Add New") { _, _ ->
                navigateToAddressFragment()
            }
            .setPositiveButton("Confirm") { _, _ ->
                showToast("Address chosen!")
                viewModel.placeOrder(address[selectedAddressIndex].addressId!!)
            }
            .show()
    }

    private fun showDialogToAddNewAddress() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Action Required!")
            .setMessage("You have no address saved!")
            .setPositiveButton("Add new address") { _, _ ->
                navigateToAddressFragment()
            }
            .setNeutralButton("Cancel") { _, _ ->
                showToast("Cancelled!")
            }.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToAddressFragment(){
        findNavController().navigate(CartFragmentDirections.actionCartFragmentToAddressFragment())
    }
}