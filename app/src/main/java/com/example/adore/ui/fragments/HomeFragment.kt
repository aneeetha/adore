package com.example.adore.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.adore.R
import com.example.adore.adapters.HomeSliderAdapter
import com.example.adore.databinding.FragmentHomeBinding
import com.example.adore.databsae.AdoreDatabase
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.dialog.LoginDialog
import com.example.adore.ui.dialogListener.LoginDialogListener
import com.example.adore.ui.viewmodels.HomeViewModel
import com.example.adore.ui.viewmodels.factory.HomeViewModelProviderFactory
import com.example.adore.util.HomeSliderItem
import com.example.adore.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    lateinit var homeSliderAdapter: HomeSliderAdapter
    lateinit var sliderHandler: Handler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.apply {
            homeFragment = this@HomeFragment
            tvLabel.setText(R.string.seasonal_collections)
        }

        val navBar = activity?.findViewById<View>(R.id.bottom_navigation_view)
        navBar?.visibility = View.VISIBLE

        val application = requireNotNull(this.activity).application
        val adoreRepository = AdoreRepository(AdoreDatabase(application))

        val viewModelFactory = HomeViewModelProviderFactory(application, adoreRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)



        val sliderItems = mutableListOf<HomeSliderItem>()
        sliderHandler = Handler(Looper.getMainLooper())
        setUpViewPager()

        viewModel.apply {
            productsWithLabel.observe(viewLifecycleOwner, { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { productsResponse ->
                            productsResponse.products.forEach {
                                sliderItems.add(HomeSliderItem(it))
                            }
                            homeSliderAdapter.differ.submitList(sliderItems)
                        }
                    }
                    is Resource.Empty ->hideProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let { message ->
                            showSnackBarWithMessage(message)
                            Log.e("ProductsFragment", "An error occurred: $message")
                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }

            })

            currentUser.observe(viewLifecycleOwner, { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { currentUserResponse ->
                            val userId = currentUserResponse.userId
                            Log.e("HomeFragment", "${getUser(userId)}")
                            getUser(userId).observe(viewLifecycleOwner, {
                                if(it==null || userId==0L){
                                    val dialog =  LoginDialog(requireContext(), object : LoginDialogListener {
                                        override fun onLoginButtonClicked(
                                            mobileNo: String,
                                            password: String
                                        ) {
                                            validateUser(mobileNo, password)
                                        }
                                        override fun onCreateNewAccountClicked() {
                                            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSignupFragment())
                                        }
                                    })
                                    dialog.setCancelable(false)
                                    dialog.show()
                                }else {
                                    getProductsWithCustomLabel()
                                }
                            })
                        }
                    }
                    is Resource.Empty ->hideProgressBar()
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

            showSnackBarMessage.observe(viewLifecycleOwner, {
                it?.let {
                    showSnackBarWithMessage(it)
                    doneShowingSnackBarMessage()
                }
            })
        }

        homeSliderAdapter.setOnItemClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(it))
        }
        return binding.root
    }

    fun navigateToSearchFragment() {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
    }

    private fun hideProgressBar(){
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            tvLabel.visibility = View.VISIBLE
        }
    }

    private fun showProgressBar(){
        binding.apply {
            progressBar.visibility = View.VISIBLE
            tvLabel.visibility = View.INVISIBLE
        }
    }

    private fun showSnackBarWithMessage(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun setUpViewPager(){
        with(binding.homeViewPager){
            homeSliderAdapter = HomeSliderAdapter(this)
            adapter = homeSliderAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER


            registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandler.removeCallbacks(sliderRunnable)
                    sliderHandler.postDelayed(sliderRunnable, 2000)
                }
            })
        }
    }

    val sliderRunnable = Runnable {
        home_view_pager.currentItem = home_view_pager.currentItem + 1
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 2000)
    }
}