package com.example.adore.ui.fragments

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.example.adore.ui.dialog.LoginDialog
import com.example.adore.ui.dialogListener.LoginDialogListener
import com.example.adore.util.Resource

//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Adapter
//import android.widget.ArrayAdapter
//import android.widget.ListAdapter
//import android.widget.Toast
//import androidx.navigation.fragment.findNavController
//import com.example.adore.R
//import com.example.adore.databinding.FragmentCategoriesBinding
//import com.example.adore.models.enums.Category
//import com.example.adore.models.enums.Gender
//import com.example.adore.models.enums.ProductType
//
//class CategoriesFragment : Fragment() {
//
//    lateinit var binding: FragmentCategoriesBinding
//    lateinit var categoryAdapter: ListAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
//        val arguments = CategoriesFragmentArgs.fromBundle(requireArguments())
//        val categories = Category.values().filter { (it.gender == arguments.gender || it.gender== Gender.Unisex) && it.type.contains(arguments.productType) }
//        categoryAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
//
//        Log.e("Category", "$categories")
//        binding.apply {
//            lvCategoryList.adapter = categoryAdapter
//            lvCategoryList.setOnItemClickListener { adapterView, view, position, id ->
//                val selectedCategory = adapterView.getItemAtPosition(position) as Category
//                findNavController().navigate(CategoriesFragmentDirections.actionCategoriesFragmentToProductsFragment(arguments.gender, arguments.productType, selectedCategory))
//            }
//
//            ivBackIcon.setOnClickListener {
//                Log.e("Navigation", "${findNavController().currentDestination}")
//                findNavController().navigateUp()
//            }
//
//            ivCartIcon.setOnClickListener {
//                findNavController().navigate(CategoriesFragmentDirections.actionCategoriesFragmentToCartFragment())
//            }
//
//            ivFavoIcon.setOnClickListener {
//                findNavController().navigate(CategoriesFragmentDirections.actionCategoriesFragmentToFavoFragment())
//            }
//
//            ivSearchIcon.setOnClickListener {
//                findNavController().navigate(CategoriesFragmentDirections.actionCategoriesFragmentToSearchFragment())
//            }
//        }
//        return binding.root
//    }
//
//}
//
//viewModelShared.apply{
//    currentUser.observe(viewLifecycleOwner, { response ->
//        when (response) {
//            is Resource.Success -> {
//                response.data?.let { currentUserResponse ->
//                    if (currentUserResponse.userId == 0L) {
//                        Log.e("UserProfileFragment", "${currentUserResponse.userId}")
//                        hideActionViews()
//                        LoginDialog(requireContext(), object : LoginDialogListener {
//                            override fun onLoginButtonClicked(
//                                mobileNo: String,
//                                password: String
//                            ) {
//                                validateUser(mobileNo, password)
//                            }
//                            override fun onCreateNewAccountClicked() {
//                                findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToSignupFragment())
//                            }
//                        }).show()
//                    } else{
//                        showActionViews()
//                    }
//                }
//            }
//            is Resource.Empty -> showSnackBarWithMessage("Nothing to show!")
//            is Resource.Error -> {
//                response.message?.let { message ->
//                    showSnackBarWithMessage(message)
//                }
//            }
//            is Resource.Loading -> {
//                hideActionViews()
//            }
//        }
//
//    })
//    showActionViews.observe(viewLifecycleOwner, {
//        it?.let {
//            showActionViews()
//            doneShowingActionViews()
//        }
//    })
//
//    showSnackBarMessage.observe(viewLifecycleOwner, {
//        it?.let {
//            showSnackBarWithMessage(it)
//            doneShowingSnackBar()
//        }
//    })
//}

//
//    fun doneShowingActionViews(){
//        _showActionViews.value = null
//    }
//
//
//    fun getCurrentUser() = viewModelScope.launch{
//        safeGetCurrentUserCall()
//    }
//
//    private suspend fun safeGetCurrentUserCall(){
//        _currentUser.value = Resource.Loading()
//        try{
//            if(hasInternetConnection()){
//                val response = adoreRepository.getCurrentUser()
//                _currentUser.value = handleResponse(response)
//                Log.e("Homee", "${currentUser.value?.data?.userId}")
//            }else{
//                _currentUser.value = Resource.Error("No internet connection :(")
//            }
//        }catch(t: Throwable){
//            when(t){
//                is IOException -> _currentUser.postValue(Resource.Error("Network Failure!"))
//                else -> _currentUser.postValue(Resource.Error("Conversion Error!"))
//            }
//        }
//    }
//
//    private fun <T: Any> handleResponse(response: Response<T>): Resource<T> =
//        if (response.isSuccessful) {
//            when (response.code()) {
//                200 -> {
//                    response.body()!!.let {
//                        Resource.Success(it)
//                    }
//                }
//                else -> {
//                    Resource.Empty()
//                }
//            }
//        } else {
//            val message = "An Error Occurred: " + when (response.code()) {
//                404 -> "404! Resource Not found"
//                500 -> "Server broken"
//                502 -> "Bad Gateway"
//                else -> "Unknown error"
//            }
//            Resource.Error(message)
//        }
//
//
//    private fun hasInternetConnection(): Boolean{
//        val connectivityManager = getApplication<AdoreApplication>().getSystemService(
//            Context.CONNECTIVITY_SERVICE
//        ) as ConnectivityManager
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            val activeNetwork = connectivityManager.activeNetwork?: return false
//            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
//            return when{
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//                else -> false
//            }
//        }else{
//            connectivityManager.activeNetworkInfo?.run {
//                return when(type){
//                    ConnectivityManager.TYPE_WIFI -> true
//                    ConnectivityManager.TYPE_MOBILE -> true
//                    ConnectivityManager.TYPE_ETHERNET -> true
//                    else -> false
//                }
//            }
//        }
//        return false
//    }
//
//    fun validateUser(mobileNo:String, password:String) = viewModelScope.launch {
//        adoreRepository.getUserWithMobileNo(mobileNo)?.let {
//            if (it.password == password) {
//                _showSnackBarMessage.value = "Logged in!"
//                _showActionViews.value = true
//                setCurrentUser(it.userId)
//            }else{
//                _showSnackBarMessage.value = "Incorrect password!"
//                getCurrentUser()
//            }
//            Log.e("AA", "${it.userId}")
//        }?:run{
//            _showSnackBarMessage.postValue("Mobile no is not registered!")
//            getCurrentUser()
//        }
//    }

//    private fun setCurrentUser(userId: Long) = viewModelScope.launch {
//        adoreRepository.setCurrentUser(userId)
//    }

