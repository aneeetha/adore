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
import com.example.adore.util.AdoreLogic

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
        viewModel.getTotalPriceInOrder(order.products)

        orderDetailsAdapter = OrderAdapter()
        binding.apply {
            rvOrders.adapter = orderDetailsAdapter
            rvOrders.layoutManager = LinearLayoutManager(activity)

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

            ivBackIcon.setOnClickListener {
                findNavController().navigateUp()
            }
        }
        orderDetailsAdapter.submitList(order.products)

        return binding.root
    }


}