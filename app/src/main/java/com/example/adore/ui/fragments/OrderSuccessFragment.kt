package com.example.adore.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adore.databinding.FragmentOrderSuccessBinding

class OrderSuccessFragment: Fragment() {

    lateinit var binding:FragmentOrderSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderSuccessBinding.inflate(inflater, container, false)
        binding.orderSuccessFragment = this
        return binding.root
    }
    fun upActionClicked(){
        findNavController().navigateUp()
    }
    fun navigateToOrderFragment(){
        findNavController().navigate(OrderSuccessFragmentDirections.actionOrderSuccessFragmentToOrderFragment())
    }
}