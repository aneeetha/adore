package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.example.adore.R
import com.example.adore.databinding.FragmentAddressDetailsBinding
import com.example.adore.models.entities.Address
import com.example.adore.models.enums.District
import com.example.adore.models.relations.UserWithAddresses
import com.google.android.material.textfield.TextInputLayout

class AddressDetailsFragment : Fragment() {

    lateinit var binding: FragmentAddressDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressDetailsBinding.inflate(inflater, container, false)
        val cityAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, District.values())

        binding.apply {
            (tilEtCity.editText as AutoCompleteTextView).setAdapter(cityAdapter)
            btnSaveDetails.setOnClickListener {
                if (tilEtDeliverTo.validateField()
                    && tilEtContact.validateField()
                    && tilEtAddressLine.validateField()
                    && tilEtAddressType.validateField() && tilEtLocality.validateField() && tilEtPincode.validateField() && tilEtCity.validateField()){
                    val address = Address(1, etAddressType.text.toString(), etAddressLine.text.toString(), etDeliverTo.text.toString(), etContact.text.toString(), etLocality.text.toString(), District.valueOf(tilEtCity.editText?.text.toString()), etPincode.text.toString().toInt())
                    //Log.e("Address", "$a")
                }
            }
        }

        return binding.root
    }

    private fun TextInputLayout.validateField() = when {
        editText?.text.toString().isEmpty() -> {
            error = "Field cannot be empty"
            false
        }
        else -> {
            error = null
            true
        }
    }


}