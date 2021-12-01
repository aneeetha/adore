package com.example.adore.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.adore.R
import com.example.adore.databinding.FragmentGenderCategoryBinding
import com.example.adore.databsae.AdoreDatabase
import com.example.adore.models.enums.Gender
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.viewmodels.SharedCategoryViewModel
import com.example.adore.ui.viewmodels.factory.SharedCategoryViewModelProviderFactory


class GenderCategoryFragment : Fragment() {


    lateinit var binding: FragmentGenderCategoryBinding
    lateinit var viewModel: SharedCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val navBar = activity?.findViewById<View>(R.id.bottom_navigation_view)
        navBar?.visibility = View.VISIBLE

        binding = FragmentGenderCategoryBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val adoreRepository = AdoreRepository(AdoreDatabase(application))

        val viewModelFactory = SharedCategoryViewModelProviderFactory(application, adoreRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SharedCategoryViewModel::class.java)

        binding.apply {
            categoryMen.setOnClickListener {
                findNavController().navigate(GenderCategoryFragmentDirections.actionGenderCategoryFragmentToProductTypeFragment(Gender.Men))
            }

            categoryWomen.setOnClickListener {
                findNavController().navigate(GenderCategoryFragmentDirections.actionGenderCategoryFragmentToProductTypeFragment(Gender.Women))
            }

            ivSearchIcon.setOnClickListener {
                findNavController().navigate(GenderCategoryFragmentDirections.actionGenderCategoryFragmentToSearchFragment())
            }
        }

        return binding.root
    }


}