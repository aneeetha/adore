package com.example.adore.ui.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adore.R
import com.example.adore.databinding.FragmentVarietiesBinding

class VarietiesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentVarietiesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_varieties, container, false)

        val args = VarietiesFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = VarietiesViewModelFactory(args.category)

        val varietiesViewModel = ViewModelProvider(this, viewModelFactory).get(VarietiesViewModel::class.java)

        binding.varietiesViewModel = varietiesViewModel
        binding.lifecycleOwner = this

        val manager = GridLayoutManager(activity, 3)



        return binding.root
    }

}