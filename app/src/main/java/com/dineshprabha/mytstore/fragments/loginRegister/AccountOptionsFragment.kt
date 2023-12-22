package com.dineshprabha.mytstore.fragments.loginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dineshprabha.mytstore.R
import com.dineshprabha.mytstore.databinding.FragmentAccountOptionsBinding
import com.dineshprabha.mytstore.databinding.FragmentIntroductionBinding

class AccountOptionsFragment: Fragment() {

    private lateinit var binding : FragmentAccountOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            buttonLoginAccountOptions.setOnClickListener {

                findNavController().navigate(R.id.action_accountOptionsFragment_to_loginFragment)
            }

            buttonRegisterAccountOptions.setOnClickListener {

                findNavController().navigate(R.id.action_accountOptionsFragment_to_registerFragment)
            }
        }
    }

}