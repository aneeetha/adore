package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adore.R
import com.example.adore.adapters.CartAdapter
import com.example.adore.ui.AdorableActivity
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_cart.*


class CartFragment : Fragment(R.layout.fragment_cart) {

    lateinit var viewModel: ProductsViewModel
    lateinit var cartAdapter: CartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navBar = activity?.findViewById<View>(R.id.bottom_navigation_view)
        navBar?.visibility = View.VISIBLE

        viewModel = (activity as AdorableActivity).viewModel
        setUpRecyclerView()
        viewModel.getCartItem()



        viewModel.cartItems.observe(viewLifecycleOwner, { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { cartResponse ->
                        cartAdapter.submitList(cartResponse.cartItems)
                        Log.e("CartFragment", "An error occurred: ${cartResponse.cartItems}")
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("CartFragment", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })

        btn_checkout.setOnClickListener {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.order_placed),
                Snackbar.LENGTH_SHORT // How long to display the message.
            ).show()
        }
    }

    private fun hideProgressBar(){
        progress_bar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        progress_bar.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView() {
        cartAdapter = CartAdapter(context)
        rv_cart.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}