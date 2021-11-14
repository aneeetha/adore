package com.example.adore.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.adore.R
import com.example.adore.databinding.ActivityAdorableBinding
import com.example.adore.databsae.ProductDatabase
import com.example.adore.repository.ProductsRepository
import com.example.adore.ui.viewmodels.factory.ProductsViewModelProviderFactory
import com.example.adore.ui.viewmodels.ProductsViewModel

class AdorableActivity : AppCompatActivity() {

    lateinit var viewModel: ProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Adore)
        val binding = DataBindingUtil.setContentView<ActivityAdorableBinding>(
            this,
            R.layout.activity_adorable
        )

        val productsRepository = ProductsRepository(ProductDatabase(this))
        val viewModelProviderFactory = ProductsViewModelProviderFactory(productsRepository)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(ProductsViewModel::class.java)

        val bottomNavigationView = binding.bottomNavigationView

        val navController = findNavController(R.id.adoreNavHostFragment)

        bottomNavigationView.setupWithNavController(navController)
    }
}
