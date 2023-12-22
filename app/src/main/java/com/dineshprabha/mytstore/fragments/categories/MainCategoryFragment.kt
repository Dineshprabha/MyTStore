package com.dineshprabha.mytstore.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dineshprabha.mytstore.R
import com.dineshprabha.mytstore.adapters.BestDealsAdapter
import com.dineshprabha.mytstore.adapters.BestProductAdapter
import com.dineshprabha.mytstore.adapters.SpecialProductsAdapter
import com.dineshprabha.mytstore.databinding.FragmentMainCategoryBinding
import com.dineshprabha.mytstore.utils.Resource
import com.dineshprabha.mytstore.utils.showBottomNavigationView
import com.dineshprabha.mytstore.viewmodel.productViewModels.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val TAG = "MainCategoryFragment"
@AndroidEntryPoint
class MainCategoryFragment: Fragment() {

    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestProductAdapter: BestProductAdapter

    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
        setupSpecialProductsRv()
        setUpBestDealsRv()
        setupBestProducts()

        specialProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, b)
        }

        bestDealsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, b)
        }

        bestProductAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, b)
        }
    }

    private fun setUpObservers() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.specialProducts.collectLatest {
                    when(it){
                        is Resource.Loading ->{
                            showLoading()
                        }
                        is Resource.Success ->{
                            specialProductsAdapter.differ.submitList(it.data)
                            hideLoading()
                        }
                        is Resource.Error ->{
                            hideLoading()
                            Log.e(TAG, it.message.toString())
                            Toast.makeText(requireContext(),it.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.bestDeals.collectLatest {
                    when(it){
                        is Resource.Loading ->{
                            showLoading()
                        }
                        is Resource.Success ->{
                            bestDealsAdapter.differ.submitList(it.data)
                            hideLoading()
                        }
                        is Resource.Error ->{
                            hideLoading()
                            Log.e(TAG, it.message.toString())
                            Toast.makeText(requireContext(),it.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.besProducts.collectLatest {
                    when(it){
                        is Resource.Loading ->{
                            binding.bestProductsProgressbar.visibility = View.VISIBLE
                        }
                        is Resource.Success ->{
                            bestProductAdapter.differ.submitList(it.data)
                            binding.bestProductsProgressbar.visibility = View.GONE
                        }
                        is Resource.Error ->{
                            binding.bestProductsProgressbar.visibility = View.GONE
                            Log.e(TAG, it.message.toString())
                            Toast.makeText(requireContext(),it.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }

        binding.nestedScrollMainCategory.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (v.getChildAt(0).bottom <= v.height + scrollY){
                    viewModel.fetchBestDeals()
                }
            })

    }

    private fun setupBestProducts() {
        bestProductAdapter = BestProductAdapter()
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }

    }

    private fun setUpBestDealsRv() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealsProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }
    }

    private fun hideLoading() {
        binding.mainCategoryProgressbar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainCategoryProgressbar.visibility = View.VISIBLE
    }

    private fun setupSpecialProductsRv() {

        specialProductsAdapter = SpecialProductsAdapter()

        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductsAdapter
        }

    }

    override fun onResume() {
        super.onResume()

        showBottomNavigationView()
    }


}