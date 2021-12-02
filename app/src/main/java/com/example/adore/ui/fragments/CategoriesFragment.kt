package com.example.adore.ui.fragments

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