package com.dineshprabha.mytstore.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dineshprabha.mytstore.R
import com.dineshprabha.mytstore.activities.ShoppingActivity
import com.dineshprabha.mytstore.databinding.FragmentIntroductionBinding
import com.dineshprabha.mytstore.fragments.loginRegister.IntroductionViewModel.Companion.ACCOUNT_OPTION_FRAGMENT
import com.dineshprabha.mytstore.fragments.loginRegister.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import com.dineshprabha.mytstore.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroductionFragment: Fragment() {

    private lateinit var  binding : FragmentIntroductionBinding
    private val viewModel by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            buttonStart.setOnClickListener {
                viewModel.startButtonClick()
                findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.navigate.collect{
                    when(it){
                        SHOPPING_ACTIVITY -> {
                            Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                        }

                        ACCOUNT_OPTION_FRAGMENT -> {
                            findNavController().navigate(it)
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

}