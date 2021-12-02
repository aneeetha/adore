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
import com.example.adore.ui.dialog.LoginDialog
import com.example.adore.ui.dialogListener.LoginDialogListener
import com.example.adore.ui.viewmodels.SharedUserProfileViewModel
import com.example.adore.ui.viewmodels.factory.UserProfileViewModelProviderFactory
import com.example.adore.util.Resource
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
        viewModelShared.apply{
            currentUser.observe(viewLifecycleOwner, { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let { currentUserResponse ->
                            if (currentUserResponse.userId == 0L) {
                                Log.e("UserProfileFragment", "${currentUserResponse.userId}")
                                hideActionViews()
                                LoginDialog(requireContext(), object : LoginDialogListener {
                                    override fun onLoginButtonClicked(
                                        mobileNo: String,
                                        password: String
                                    ) {
                                        validateUser(mobileNo, password)
                                    }
                                    override fun onCreateNewAccountClicked() {
                                        findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToSignupFragment())
                                    }
                                }).show()
                            } else{
                                showActionViews()
                            }
                        }
                    }
                    is Resource.Error -> {
                        response.message?.let { message ->
                            showSnackBarWithMessage(message)
                        }
                    }
                    is Resource.Loading -> {
                        hideActionViews()
                    }
                }

            })
            showActionViews.observe(viewLifecycleOwner, {
                it?.let {
                    showActionViews()
                    doneShowingActionViews()
                }
            })

            showSnackBarMessage.observe(viewLifecycleOwner, {
                it?.let {
                    showSnackBarWithMessage(it)
                    doneShowingSnackBar()
                }
            })
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
    fun navigateToFavoFragment(){
        findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToFavoFragment())
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

    private fun hideActionViews(){
        binding.apply {
            layoutProfileActions.visibility = View.INVISIBLE
            btnLogout.visibility = View.INVISIBLE
        }
    }

    private fun showActionViews(){
        binding.apply {
            layoutProfileActions.visibility = View.VISIBLE
            btnLogout.visibility = View.VISIBLE
        }
    }
}