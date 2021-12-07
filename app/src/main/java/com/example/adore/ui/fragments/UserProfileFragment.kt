package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.adore.R
import com.example.adore.databinding.FragmentUserProfileBinding
import com.example.adore.databsae.AdoreDatabase
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.viewmodels.SharedUserProfileViewModel
import com.example.adore.ui.viewmodels.factory.UserProfileViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar


class UserProfileFragment : Fragment() {

    lateinit var binding: FragmentUserProfileBinding
    private lateinit var viewModelShared: SharedUserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navBar = activity?.findViewById<View>(R.id.bottom_navigation_view)
        navBar?.visibility = View.VISIBLE

        binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val adoreRepository = AdoreRepository(AdoreDatabase(application))

        val viewModelFactory = UserProfileViewModelProviderFactory(application, adoreRepository)
        viewModelShared = ViewModelProvider(requireActivity(), viewModelFactory).get(SharedUserProfileViewModel::class.java)

        binding.apply {
            userProfileFragment = this@UserProfileFragment
        }
        return binding.root

    }

    fun navigateToOrdersFragment(){
        findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToOrderFragment())
    }

    fun navigateToUserAddressFragment(){
        findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToAddressFragment())
    }

    fun navigateToUserDetails(){
        findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToUserDetailsFragment())
    }

    fun logout(){
        viewModelShared.logoutCurrentUser()
        showSnackBarWithMessage("Logged Out!")
        findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToHomeFragment())
    }

    private fun showSnackBarWithMessage(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

}