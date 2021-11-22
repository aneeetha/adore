package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adore.R
import com.example.adore.adapters.CartAdapter
import com.example.adore.databinding.FragmentCartBinding
import com.example.adore.ui.AdorableActivity
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_cart.*


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
        return binding.root
    }

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
                        viewModel.getTotalPrice(cartResponse.cartItems)
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

        viewModel.totalPrice.observe(viewLifecycleOwner, { it ->
            val text = context?.getString(R.string.total_price, it.toString())
            text?.let {text->
                val dynamicText = String.format(text, "placeholder1")
                val dynamicStyledText =
                    HtmlCompat.fromHtml(dynamicText, HtmlCompat.FROM_HTML_MODE_COMPACT)
                binding.tvTotalPrice.text = dynamicStyledText
            }
            //binding.tvTotalPrice.text = context?.getString(R.string.total_price, it.toString())
        })

        cartAdapter.setOnQuantityItemClickListener { quantity, cartItemId ->
            viewModel.cartItemQuantityChanged(quantity.toInt(), cartItemId)
        }

        cartAdapter.setOnRemoveButtonClickListener { cartItemId->
            viewModel.removeCartItem(cartItemId)
        }

        binding.btnCheckout.setOnClickListener {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.order_placed),
                Snackbar.LENGTH_SHORT // How long to display the message.
            ).show()
        }
    }

    private fun hideProgressBar(){
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            btnCheckout.visibility = View.VISIBLE
            tvTotalPrice.visibility = View.VISIBLE
            rvCart.visibility = View.VISIBLE
        }
    }

    private fun showProgressBar(){
        binding.apply {
            progressBar.visibility = View.VISIBLE
            btnCheckout.visibility = View.INVISIBLE
            tvTotalPrice.visibility = View.INVISIBLE
            rvCart.visibility = View.INVISIBLE
        }
    }

    private fun setUpRecyclerView() {
        cartAdapter = CartAdapter()
        rv_cart.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}