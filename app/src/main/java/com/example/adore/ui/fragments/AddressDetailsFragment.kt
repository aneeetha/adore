package com.example.adore.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.adore.R
import com.example.adore.databinding.FragmentAddressDetailsBinding
import com.example.adore.databsae.AdoreDatabase
import com.example.adore.models.entities.AddressDetailUpdate
import com.example.adore.models.enums.District
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.viewmodels.SharedUserProfileViewModel
import com.example.adore.ui.viewmodels.factory.UserProfileViewModelProviderFactory
import com.google.android.material.textfield.TextInputLayout

class AddressDetailsFragment : Fragment() {

    lateinit var binding: FragmentAddressDetailsBinding
    lateinit var viewModelShared: SharedUserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressDetailsBinding.inflate(inflater, container, false)
        val cityAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, District.values())

        val currentAddress = AddressDetailsFragmentArgs.fromBundle(requireArguments()).address

        val application = requireNotNull(this.activity).application
        val adoreRepository = AdoreRepository(AdoreDatabase(application))

        val viewModelFactory = UserProfileViewModelProviderFactory(application, adoreRepository)
        viewModelShared = ViewModelProvider(requireActivity(), viewModelFactory).get(SharedUserProfileViewModel::class.java)


        binding.apply {
            currentAddress.addressType?.let { etAddressType.setText(it) }
            currentAddress.name?.let { etDeliverTo.setText(it) }
            currentAddress.contact?.let { etContact.setText(it) }
            currentAddress.addressLine?.let { etAddressLine.setText(it) }
            currentAddress.locality?.let { etLocality.setText(it) }
            currentAddress.city?.let { tilEtCity.editText?.setText(it.name) }
            currentAddress.pincode?.let { etPincode.setText(it.toString()) }

            (tilEtCity.editText as AutoCompleteTextView).setAdapter(cityAdapter)
            btnSaveDetails.setOnClickListener {
                if (tilEtDeliverTo.validateField()
                    && tilEtContact.validateField()
                    && tilEtAddressLine.validateField()
                    && tilEtAddressType.validateField() && tilEtLocality.validateField() && tilEtPincode.validateField() && tilEtCity.validateField()){
                    val address = AddressDetailUpdate(currentAddress.addressId, etAddressType.text.toString(), etAddressLine.text.toString(), etDeliverTo.text.toString(), etContact.text.toString(), etLocality.text.toString(), District.valueOf(tilEtCity.editText?.text.toString()), etPincode.text.toString().toInt())
                    viewModelShared.updateAddress(address)
                }
            }

            ivBackIcon.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        viewModelShared.navigateToAddressFragment.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(AddressDetailsFragmentDirections.actionAddressDetailsFragmentToAddressFragment())
                viewModelShared.doneNavigatingToAddressFragment()
            }
        })

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