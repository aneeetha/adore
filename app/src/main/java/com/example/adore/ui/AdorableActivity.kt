package com.example.adore.ui

import android.app.UiModeManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.adore.R
import com.example.adore.databinding.ActivityAdorableBinding
import com.example.adore.databsae.AdoreDatabase
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.fragments.HomeFragmentDirections
import com.example.adore.ui.viewmodels.factory.ProductsViewModelProviderFactory
import com.example.adore.ui.viewmodels.ProductsViewModel
import com.example.adore.util.Resource

class AdorableActivity : AppCompatActivity() {

    lateinit var viewModel: ProductsViewModel
    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Adore)

        val binding = DataBindingUtil.setContentView<ActivityAdorableBinding>(
            this,
            R.layout.activity_adorable
        )

        val adoreRepository = AdoreRepository(AdoreDatabase(this))
        val viewModelProviderFactory =
            ProductsViewModelProviderFactory(application, adoreRepository)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(ProductsViewModel::class.java)


        val bottomNavigationView = binding.bottomNavigationView
        navController = findNavController(R.id.adoreNavHostFragment)
        bottomNavigationView.setupWithNavController(navController)
    }

}
