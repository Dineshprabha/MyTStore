package com.dineshprabha.mytstore.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dineshprabha.mytstore.R
import com.dineshprabha.mytstore.data.Category
import com.dineshprabha.mytstore.utils.Resource
import com.dineshprabha.mytstore.viewmodel.productViewModels.CategoryViewModel
import com.dineshprabha.mytstore.viewmodel.productViewModels.factory.BaseCategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TableFragment : BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(firestore, Category.Table)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.offerProducts.collectLatest {
                    when(it){
                        is Resource.Loading ->{
                            showOfferLoading()
                        }
                        is Resource.Success ->{
                            offerAdapter.differ.submitList(it.data)
                            hideOfferLoading()
                        }
                        is Resource.Error ->{
                            hideBestLoading()
                            Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                        }
                        else -> Unit
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.bestProducts.collectLatest {
                    when(it){
                        is Resource.Loading ->{
                            showBestLoading()
                        }
                        is Resource.Success ->{
                            hideBestLoading()
                            bestProductAdapter.differ.submitList(it.data)
                        }
                        is Resource.Error ->{
                            hideBestLoading()
                            Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                        }
                        else -> Unit
                    }
                }
            }
        }

    }

    override fun onBestProductsPagingRequest() {

    }

    override fun onOfferPagingRequest() {


    }

}