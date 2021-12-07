package com.example.adore.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.adore.R
import com.example.adore.databinding.FragmentAddressDetailsBinding
import com.example.adore.databsae.AdoreDatabase
import com.example.adore.models.entities.Address
import com.example.adore.models.entities.AddressDetailUpdate
import com.example.adore.models.enums.District
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.viewmodels.SharedUserProfileViewModel
import com.example.adore.ui.viewmodels.factory.UserProfileViewModelProviderFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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

        val application = requireNotNull(this.activity).application
        val adoreRepository = AdoreRepository(AdoreDatabase(application))

        val viewModelFactory = UserProfileViewModelProviderFactory(application, adoreRepository)
        viewModelShared = ViewModelProvider(requireActivity(), viewModelFactory).get(SharedUserProfileViewModel::class.java)

        val currentAddress = AddressDetailsFragmentArgs.fromBundle(requireArguments()).address

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
                saveDetails(currentAddress)
            }

            btnDiscard.setOnClickListener {
                viewModelShared.deleteAddress(currentAddress)
                showSnackBarWithMessage(getString(R.string.address_deleted))
                findNavController().navigate(AddressDetailsFragmentDirections.actionAddressDetailsFragmentToUserProfileFragment())
            }

            ivBackIcon.setOnClickListener {
                showAlertDialogToSave(currentAddress)
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    showAlertDialogToSave(currentAddress)
                }
            })
        }

        viewModelShared.navigateToUserProfile.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(AddressDetailsFragmentDirections.actionAddressDetailsFragmentToUserProfileFragment())
                viewModelShared.doneNavigatingToUserProfile()
            }
        })

        return binding.root
    }

    private fun showAlertDialogToSave(currentAddress: Address){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Action Required!")
            .setPositiveButton("Save"){ _, _ ->
                saveDetails(currentAddress)
            }
            .setNeutralButton("Discard"){ _, _ ->
                viewModelShared.deleteAddress(currentAddress)
                showSnackBarWithMessage("Address discarded!")
                findNavController().navigate(AddressDetailsFragmentDirections.actionAddressDetailsFragmentToUserProfileFragment())
            }.show()
    }

    private fun allFieldsValid(): Boolean {
        var fields: List<TextInputLayout>
        binding.apply {
            fields = listOf(tilEtDeliverTo, tilEtContact, tilEtAddressLine, tilEtAddressType, tilEtLocality, tilEtPincode, tilEtCity)
        }
        return !fields.map { it.validateField() }.contains(false)
    }

    private fun saveDetails(currentAddress: Address){
        binding.apply {
            if (allFieldsValid()){
                val address = AddressDetailUpdate(currentAddress.addressId, etAddressType.text.toString(), etAddressLine.text.toString(), etDeliverTo.text.toString(), etContact.text.toString(), etLocality.text.toString(), District.valueOf(tilEtCity.editText?.text.toString()), etPincode.text.toString().toInt())
                showSnackBarWithMessage(getString(R.string.address_added))
                viewModelShared.updateAddress(address)
            }
        }
    }


    private fun TextInputLayout.validateField() = when {
        editText?.text.toString().isEmpty() -> {
            error = "Field cannot be empty"
            false
        }
        editText?.text.toString().length<5 ->{
            error = "Field's value is too short!"
            false
        }
        else -> {
            error = null
            true
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