package com.dineshprabha.mytstore.fragments.categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dineshprabha.mytstore.R
import com.dineshprabha.mytstore.adapters.BestProductAdapter
import com.dineshprabha.mytstore.databinding.FragmentBaseCategoryBinding


open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {

    private lateinit var binding : FragmentBaseCategoryBinding
    protected val offerAdapter: BestProductAdapter by lazy { BestProductAdapter()  }
    protected val bestProductAdapter: BestProductAdapter by lazy { BestProductAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (::binding.isInitialized){
            setupOfferRv()
            setupBestProductsRv()

            binding.rvOfferProducts.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1) && dx != 0){
                        onOfferPagingRequest()
                    }
                }
            })

            binding.nestedScrollBaseCategory.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                    if (v.getChildAt(0).bottom <= v.height + scrollY){
                        onBestProductsPagingRequest()
                    }
                })
        }

    }

    fun showOfferLoading(){
        if (::binding.isInitialized){
            binding.offerProductsProgressBar.visibility = View.VISIBLE
        }
    }

    fun hideOfferLoading(){
        if (::binding.isInitialized){
            binding.offerProductsProgressBar.visibility = View.GONE
        }
    }

    fun showBestLoading(){
        if (::binding.isInitialized){
            binding.bestProductsProgressBar.visibility = View.VISIBLE
        }
    }

    fun hideBestLoading(){
        if (::binding.isInitialized){
            binding.bestProductsProgressBar.visibility = View.GONE
        }
    }

    open fun onOfferPagingRequest(){

    }

    open fun onBestProductsPagingRequest(){

    }

    private fun setupBestProductsRv() {
        if (::binding.isInitialized){
            binding.rvBestProducts.apply {
                layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL, false)
                adapter = bestProductAdapter
            }
        }
    }

    private fun setupOfferRv() {
        if (::binding.isInitialized) {
            binding.rvOfferProducts.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = offerAdapter
            }
        }else{
        }

    }

}