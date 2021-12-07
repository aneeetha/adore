package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adore.R
import com.example.adore.adapters.AddressAdapter
import com.example.adore.databinding.FragmentAddressBinding
import com.example.adore.databsae.AdoreDatabase
import com.example.adore.databsae.SessionManager
import com.example.adore.models.entities.Address
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.viewmodels.SharedUserProfileViewModel
import com.example.adore.ui.viewmodels.factory.UserProfileViewModelProviderFactory
import com.example.adore.util.Resource
import com.google.android.material.snackbar.Snackbar

class AddressFragment : Fragment() {

    lateinit var binding: FragmentAddressBinding
    lateinit var addressesAdapter: AddressAdapter
    lateinit var viewModelShared: SharedUserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navBar = activity?.findViewById<View>(R.id.bottom_navigation_view)
        navBar?.visibility = View.GONE

        binding = FragmentAddressBinding.inflate(inflater, container, false)
        setupRecyclerView()

        val application = requireNotNull(this.activity).application
        val adoreRepository = AdoreRepository(AdoreDatabase(application))
        val sessionManager = SessionManager(application)

        val viewModelFactory = UserProfileViewModelProviderFactory(application, adoreRepository)
        viewModelShared = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(SharedUserProfileViewModel::class.java)

        addressesAdapter.setOnItemClickListener {
            findNavController().navigate(
                AddressFragmentDirections.actionAddressFragmentToAddressDetailsFragment(
                    it
                )
            )
        }

        binding.apply {
            ivBackIcon.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        viewModelShared.apply {

            binding.btnAddNewAddress.setOnClickListener {
                insertNewAddress(
                    Address(
                        sessionManager.getUserId(),
                        "address ${(0 until 10).random()}"
                    )
                )

                getLastInsertedAddress()
                    .observe(viewLifecycleOwner, { data ->
                        data?.let { address ->
                            findNavController().navigate(
                                AddressFragmentDirections.actionAddressFragmentToAddressDetailsFragment(
                                    address
                                )
                            )
                        }
                    })
            }
            getAddressesOfCurrentUser()
                .observe(viewLifecycleOwner, {
                    it?.let { addressesAdapter.submitList(it.addresses) }
                        ?: Log.e("AddressFragment", "0")
                })
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        addressesAdapter = AddressAdapter()
        binding.rvAddresses.apply {
            adapter = addressesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}