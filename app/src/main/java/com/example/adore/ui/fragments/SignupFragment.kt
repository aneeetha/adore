package com.example.adore.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.adore.R
import com.example.adore.databinding.FragmentSignupBinding
import com.example.adore.databsae.AdoreDatabase
import com.example.adore.models.entities.User
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.viewmodels.SignupViewModel
import com.example.adore.ui.viewmodels.factory.SignupViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_signup.*
import java.util.*


class SignupFragment : Fragment() {

    lateinit var viewModel: SignupViewModel
    lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val adoreRepository = AdoreRepository(AdoreDatabase(application))

        val viewModelFactory = SignupViewModelProviderFactory(adoreRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SignupViewModel::class.java)

        binding.apply {
            btnSignup.setOnClickListener {
                if (validateName() && validateMobileNo() && validatePassword() && validateConfirmPassword()) {
                    val user = User(
                        ftilEtName.editText?.text.toString(),
                        ftilEtMobileNo.editText?.text.toString(),
                        ftilEtPassword.editText?.text.toString(),
                        Calendar.getInstance().timeInMillis * (1 until 50).random()
                    )
                    Log.e("Activity", user.password)
                    viewModel.insertNewUser(user)
                }
            }

            ivBackIcon.setOnClickListener {
                Log.e("Navigation", "${findNavController().currentDestination}")
                findNavController().navigateUp()
            }

        }
        viewModel.apply {
            navigateToHomeFragment.observe(viewLifecycleOwner, {
                it?.let{
                    findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToHomeFragment())
                    doneNavigatingToHomeFragment()
                }
            })

            showSnackBarMessage.observe(viewLifecycleOwner, {
                it?.let{
                    showSnackBarWithMessage(it)
                    doneShowingSnackBar()
                }
            })
        }



        return binding.root

    }

    private fun showSnackBarWithMessage(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }


    private fun validateName() = with(binding.ftilEtName) {
        when {
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

    private fun validateMobileNo(): Boolean = with(binding.ftilEtMobileNo) {
        val whiteSpace = Regex("(?=\\S+$)")
        when {
            editText?.text.toString().isEmpty() -> {
                error = "Field cannot be empty"
                false
            }
            editText?.text.toString().length != 10 -> {
                error = "Field must contain exactly 10 values"
                false
            }
            editText?.text.toString().matches(whiteSpace) -> {
                error = "White Spaces are not allowed"
                false
            }
            else -> {
                error = null
                true
            }
        }
    }

    private fun validatePassword() = with(binding.ftilEtPassword) {
        val passwordVal = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")
        when {
            editText?.text.toString().isEmpty() -> {
                error = "Field cannot be empty"
                false
            }
            editText?.text.toString().length < 5 -> {
                error = "Password too weak"
                false
            }
            else -> {
                error = null
                true
            }
        }
    }

    private fun validateConfirmPassword() = with(binding.ftilEtConfirmPassword) {
        when {
            editText?.text.toString().isEmpty() -> {
                error = "Field cannot be empty"
                false
            }
            editText?.text.toString() != binding.ftilEtPassword.editText?.text.toString() -> {
                error = "Password does not match"
                false
            }
            else -> {
                error = null
                true
            }
        }
    }

    private fun validateEmail() = with(binding.ftilEtConfirmPassword) {
        val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        when {
            editText?.text.toString().isEmpty() -> {
                error = "Field cannot be empty"
                false
            }
            editText?.text.toString().matches(emailPattern) -> {
                error = "Enter valid email"
                false
            }
            else -> {
                error = null
                true
            }
        }
    }

}

//^                 # start-of-string
//(?=.*[0-9])       # a digit must occur at least once
//(?=.*[a-z])       # a lower case letter must occur at least once
//(?=.*[A-Z])       # an upper case letter must occur at least once
//(?=.*[@#$%^&+=])  # a special character must occur at least once you can replace with your special characters
//(?=\\S+$)          # no whitespace allowed in the entire string
//.{4,}             # anything, at least six places though
//$                 # end-of-string