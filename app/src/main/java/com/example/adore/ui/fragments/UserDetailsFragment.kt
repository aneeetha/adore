package com.example.adore.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.adore.R
import com.example.adore.databinding.FragmentUserDetailsBinding
import com.example.adore.databsae.AdoreDatabase
import com.example.adore.models.entities.User
import com.example.adore.models.enums.Gender
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.viewmodels.SharedUserProfileViewModel
import com.example.adore.ui.viewmodels.factory.UserProfileViewModelProviderFactory
import com.example.adore.util.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.util.*


class UserDetailsFragment : Fragment() {

    lateinit var binding: FragmentUserDetailsBinding
    lateinit var viewModelShared: SharedUserProfileViewModel
    private lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false)

        val navBar = activity?.findViewById<View>(R.id.bottom_navigation_view)
        navBar?.visibility = View.GONE

        val application = requireNotNull(this.activity).application
        val adoreRepository = AdoreRepository(AdoreDatabase(application))

        val viewModelFactory = UserProfileViewModelProviderFactory(application, adoreRepository)
        viewModelShared = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(SharedUserProfileViewModel::class.java)


        viewModelShared.getCurrentUser()

        binding.apply {
            ivBackIcon.setOnClickListener {
                showAlertDialogToSave()
            }

            etDob.keyListener = null
            etDob.setOnFocusChangeListener { view, b ->
                if (b) {
                    showDatePickerDialog()
                }
            }
            rgGender.setOnCheckedChangeListener { group, checkId ->
               val gender: Gender =  when(checkId){
                    R.id.rb_male -> Gender.Men
                    else -> Gender.Women
                }
                Log.e("User Gender", "$gender")
                viewModelShared.gender = gender
            }
            btnSaveDetails.setOnClickListener {
                saveDetails()
            }
        }

        viewModelShared.showSnackBarMessage.observe(viewLifecycleOwner, {
            it?.let{
                showSnackBarWithMessage(it)
                viewModelShared.doneShowingSnackBar()
            }
        })


        viewModelShared.currentUser.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { data->
                        viewModelShared.getUserDetails(data.userId).observe(viewLifecycleOwner, { user ->
                            if(user!=null) {
                                currentUser = user
                                binding.apply {
                                    etName.setText(user.userName)
                                    etMobileNo.setText(user.mobileNo)
                                    user.apply {
                                        dob?.let { etDob.setText(getDateFormatted(it)) }
                                        gender?.let { if(it.name=="Women") rbFemale.isChecked = true else rbMale.isChecked=true  }
                                        email?.let { etEmailId.setText(it) }
                                    }
                                }
                            }
                        })
                    }
                }
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

    private fun saveDetails(){
        binding.apply {
            if(validateEmail()){
                val email = if(tilEtEmailId.editText?.text.toString().isEmpty()) null else tilEtEmailId.editText?.text.toString()
                viewModelShared.saveUserDetails(currentUser.userId, email)
                showSnackBarWithMessage("Details saved successfully!")
                findNavController().navigateUp()
            }
        }
    }

    private fun showAlertDialogToSave(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Wanna save changes?")
            .setPositiveButton("Yes"){ _, _ ->
                saveDetails()
            }
            .setNeutralButton("No"){ _, _ ->
                showSnackBarWithMessage("Changes not saved!")
                findNavController().navigateUp()
            }.show()
    }

    private fun showDatePickerDialog() {
        DatePickerDialog(
            requireContext(),
            { p0, year, month, day ->
                val newDate: Calendar = Calendar.getInstance()
                newDate.set(year, month , day)
                viewModelShared.dob = newDate.time
                binding.etDob.setText(getDateFormatted(newDate.time))
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        ).run {
            datePicker.minDate = -631171800000
            datePicker.maxDate = System.currentTimeMillis()-1000
            show()
        }
    }

    private fun getDateFormatted(date: Date) = DateFormat.getDateInstance().format(date)

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.llUserDetails.visibility = View.VISIBLE
        binding.btnSaveDetails.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.llUserDetails.visibility = View.INVISIBLE
        binding.btnSaveDetails.visibility = View.INVISIBLE
    }

    private fun showSnackBarWithMessage(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT // How long to display the message.
        ).show()
    }

    private fun validateEmail() = with(binding.tilEtEmailId){
        val emailPattern = Regex("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
            //"^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+$"
        editText?.text.toString()
        if(editText?.text.toString().matches(emailPattern) || editText?.text.toString().isEmpty()){
            error=null
            true
        }else{
            error="Enter valid email"
            false
        }
    }
}