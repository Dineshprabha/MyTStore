package com.dineshprabha.mytstore.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dineshprabha.mytstore.R
import com.dineshprabha.mytstore.activities.ShoppingActivity
import com.dineshprabha.mytstore.adapters.ColorsAdapter
import com.dineshprabha.mytstore.adapters.SizesAdapter
import com.dineshprabha.mytstore.adapters.ViewPager2Images
import com.dineshprabha.mytstore.databinding.FragmentProductDetailsBinding
import com.dineshprabha.mytstore.utils.hideBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductDetailsFragment : Fragment() {

    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding : FragmentProductDetailsBinding

    private val viewPagerAdapter by lazy { ViewPager2Images() }
    private val sizeAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupSizesRv()
        setupColorsRv()
        setupViewPager()

        binding.apply {

            tvProductName.text = product.name
            tvProductPrice.text = "Rs. ${product.price}"
            tvProductDescription.text = product.description

            if (product.colors.isNullOrEmpty())
                tvProductColors.visibility = View.INVISIBLE

            if (product.sizes.isNullOrEmpty())
                tvProductSize.visibility = View.INVISIBLE

            imageClose.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        viewPagerAdapter.differ.submitList(product.images)
        product.colors?.let {
            colorsAdapter.differ.submitList(it)
        }
        product.sizes?.let {
            sizeAdapter.differ.submitList(it)
        }
    }

    private fun setupViewPager() {
        binding.apply {
            viewPagerProductImages.adapter = viewPagerAdapter
        }
    }

    private fun setupColorsRv() {
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupSizesRv() {
        binding.rvSizes.apply {
            adapter = sizeAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }
}