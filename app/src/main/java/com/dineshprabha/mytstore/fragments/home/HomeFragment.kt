package com.dineshprabha.mytstore.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dineshprabha.mytstore.R
import com.dineshprabha.mytstore.adapters.HomeViewpagerAdapter
import com.dineshprabha.mytstore.databinding.FragmentHomeBinding
import com.dineshprabha.mytstore.fragments.categories.ChairFragment
import com.dineshprabha.mytstore.fragments.categories.CupboardFragment
import com.dineshprabha.mytstore.fragments.categories.FurnitureFragment
import com.dineshprabha.mytstore.fragments.categories.MainCategoryFragment
import com.dineshprabha.mytstore.fragments.categories.TableFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment: Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment = arrayListOf(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            FurnitureFragment()
        )

        binding.viewpagerHome.isUserInputEnabled = false

        val viewPager2Adapter = HomeViewpagerAdapter(categoriesFragment, childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome){tab, position ->
            when(position){
                0 -> tab.text = "Home"
                1 -> tab.text = "Chair"
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Table"
                4 -> tab.text = "Furniture"
            }

        }.attach()
    }
}