package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.navigation.fragment.findNavController
import com.example.adore.R
import com.example.adore.databinding.FragmentOrderBinding
import com.example.adore.ui.AdorableActivity
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.util.Resource
import com.google.android.material.snackbar.Snackbar


class OrderFragment : Fragment() {

    lateinit var viewModel: ProductsViewModel
    lateinit var orderAdapter: ListAdapter
    lateinit var binding: FragmentOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navBar = activity?.findViewById<View>(R.id.bottom_navigation_view)
        navBar?.visibility = View.GONE

        binding = FragmentOrderBinding.inflate(inflater, container, false)
        viewModel = (activity as AdorableActivity).viewModel
        viewModel.getOrders()

        binding.apply {
            ivBackIcon.setOnClickListener {
                Log.e("Navigation", "${findNavController().currentDestination}")
                findNavController().navigateUp()
            }
        }

        viewModel.orders.observe(viewLifecycleOwner, {response->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideNoResultFound()
                    response.data?.let { orderResponse ->
                        val ordersId: List<String> = orderResponse.orders.map { order ->
                            requireContext().getString(
                                R.string.order_id,
                                order._id
                            )
                        }
                        orderAdapter = ArrayAdapter(requireContext(), R.layout.list_text_view, ordersId)
                        binding.apply {
                            lvOrders.adapter = orderAdapter
                            lvOrders.setOnItemClickListener { _, _, position, _ ->
                                findNavController().navigate(
                                    OrderFragmentDirections.actionOrderFragmentToOrderDetailsFragment(orderResponse.orders[position]))
                            }
                        }
                    }
                }
                is Resource.Empty->showNoResultFound()
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

        return binding.root
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
        }
    }

    private fun showNoResultFound(){
        hideProgressBar()
        binding.apply {
            lvOrders.visibility = View.INVISIBLE
            imgNotFound.visibility = View.VISIBLE
        }
    }

    private fun hideNoResultFound(){
        binding.apply {
            lvOrders.visibility = View.VISIBLE
            imgNotFound.visibility = View.GONE
        }
    }


    private fun showSnackBarWithMessage(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }


}