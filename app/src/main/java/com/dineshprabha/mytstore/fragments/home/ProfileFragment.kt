package com.dineshprabha.mytstore.fragments.home

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dineshprabha.mytstore.R
import com.dineshprabha.mytstore.activities.LoginRegisterActivity
import com.dineshprabha.mytstore.databinding.FragmentProfileBinding
import com.dineshprabha.mytstore.utils.Resource
import com.dineshprabha.mytstore.utils.showBottomNavigationView
import com.dineshprabha.mytstore.viewmodel.productViewModels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.grpc.android.BuildConfig
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            constraintProfile.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
            }

            linearAllOrders.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_allOrdersFragment)
            }

            linearBilling.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToBillingFragment(0f, emptyArray(), false)
                findNavController().navigate(action)
            }

            linearLogOut.setOnClickListener {
                viewModel.logOut()
                val intent = Intent(requireActivity(), LoginRegisterActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED){
                    viewModel.user.collectLatest {
                        when(it){
                            is Resource.Loading -> {
                                binding.progressbarSettings.visibility = View.VISIBLE
                            }
                            is Resource.Success -> {
                                binding.progressbarSettings.visibility = View.GONE
                                Glide.with(requireView()).load(it.data!!.imagePath).error(ColorDrawable(Color.BLACK))
                                    .into(binding.imageUser)
                                binding.tvUserName.text = "${it.data.firstName} ${it.data.lastName}"
                            }
                            is Resource.Error -> {
                                binding.progressbarSettings.visibility = View.GONE
                                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                            else -> Unit
                        }
                    }
                }
            }


        }


    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }


}