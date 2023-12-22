package com.dineshprabha.mytstore.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
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
import com.dineshprabha.mytstore.databinding.FragmentLoginBinding
import com.dineshprabha.mytstore.dialog.setupBottomSheetDialog
import com.dineshprabha.mytstore.utils.Resource
import com.dineshprabha.mytstore.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment: Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            buttonLogin.setOnClickListener {
                val email = edEmailLogin.text.toString().trim()
                val password = edPasswordLogin.text.toString()
                viewModel.login(email,password)
            }

            tvNotHaveAccount.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }


            tvForgotPasswordLogin.setOnClickListener {
                setupBottomSheetDialog { email ->
                    viewModel.resetPassword(email)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.resetPassword.collect{
                    when(it){
                        is Resource.Loading -> {

                        }
                        is Resource.Success ->{
                            Snackbar.make(requireView(), "Reset link was sent to your email", Snackbar.LENGTH_SHORT)
                        }
                        is Resource.Error ->{
                            Snackbar.make(requireView(), "Error:${it.message}", Snackbar.LENGTH_SHORT)
                        }
                        else ->Unit
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.login.collect{
                    when(it){
                        is Resource.Loading -> {
                            binding.buttonLogin.startAnimation()
                        }
                        is Resource.Success ->{
                            binding.buttonLogin.revertAnimation()
                            Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                        }
                        is Resource.Error ->{
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            binding.buttonLogin.revertAnimation()
                        }
                        else ->Unit
                    }
                }
            }
        }

    }

}