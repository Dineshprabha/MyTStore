package com.dineshprabha.mytstore.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.dineshprabha.mytstore.R
import com.dineshprabha.mytstore.databinding.ActivityLoginRegisterBinding
import com.dineshprabha.mytstore.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRegisterActivity : AppCompatActivity() {

    val TAG = LoginRegisterActivity::class.java.name
    private lateinit var registerViewModel: RegisterViewModel

    private lateinit var binding: ActivityLoginRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

//        val navController = findNavController(R.id.nav_host_login_register_navigation)

//        val appBarConfiguration = AppBarConfiguration(setOf(R.id.introductionFragment,
//            R.id.addNoteFragment))
//        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
}