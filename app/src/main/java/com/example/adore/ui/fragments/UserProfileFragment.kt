package com.example.adore.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.adore.databinding.FragmentUserProfileBinding
import com.example.adore.databsae.AdoreDatabase
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.viewmodels.UserProfileViewModel
import com.example.adore.ui.viewmodels.factory.UserProfileViewModelProviderFactory


class UserProfileFragment : Fragment() {

    lateinit var binding: FragmentUserProfileBinding
    lateinit var viewModel: UserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val adoreRepository = AdoreRepository(AdoreDatabase(application))

        val viewModelFactory = UserProfileViewModelProviderFactory(application, adoreRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserProfileViewModel::class.java)

        binding.apply {
            userProfileFragment = this@UserProfileFragment
        }

        return binding.root
    }

    fun navigateToUserAddressFragment(){
        findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToAddressDetailsFragment())
    }

    fun navigateToUserDetails(){
        findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToUserDetailsFragment())
    }
    fun navigateToFavoFragment(){
        findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToFavoFragment())
    }
    fun logout(){
        viewModel.logoutCurrentUser()
        findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToHomeFragment())
    }
}