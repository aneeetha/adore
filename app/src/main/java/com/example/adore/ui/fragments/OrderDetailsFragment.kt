package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adore.R
import com.example.adore.adapters.OrderAdapter
import com.example.adore.databinding.FragmentOrderDetailsBinding
import com.example.adore.ui.AdorableActivity
import com.example.adore.ui.viewmodels.ProductsViewModel

class OrderDetailsFragment: Fragment() {
    lateinit var viewModel: ProductsViewModel
    lateinit var orderDetailsAdapter: OrderAdapter
    lateinit var binding: FragmentOrderDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        viewModel = (activity as AdorableActivity).viewModel
        val order = OrderDetailsFragmentArgs.fromBundle(requireArguments()).order
        Log.e("OrderDetailsFragment", "$order")
        orderDetailsAdapter = OrderAdapter(viewModel)
        binding.apply {
            rvOrders.adapter = orderDetailsAdapter
            rvOrders.layoutManager = LinearLayoutManager(activity)

            context?.getString(R.string.delivery_charge, order.deliveryCharge.toString())?.let {
                tvDeliveryCharge.text = HtmlCompat.fromHtml(String.format(it, "placeholder1"), HtmlCompat.FROM_HTML_MODE_COMPACT)
            }
            context?.getString(R.string.total_price, order.totalPrice.plus(order.deliveryCharge).toString())?.let {
                tvTotalPrice.text = HtmlCompat.fromHtml(String.format(it, "placeholder1"), HtmlCompat.FROM_HTML_MODE_COMPACT)
            }
            ivBackIcon.setOnClickListener {
                findNavController().navigateUp()
            }
        }
        orderDetailsAdapter.submitList(order.products)

        return binding.root
    }


}