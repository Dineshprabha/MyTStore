package com.dineshprabha.mytstore.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.dineshprabha.mytstore.R
import com.dineshprabha.mytstore.activities.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigationView(){
    val bottomNavigationView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(
        R.id.bottomNavigation
    )
    bottomNavigationView.visibility= View.GONE
}

fun Fragment.showBottomNavigationView(){
    val bottomNavigationView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(
        R.id.bottomNavigation
    )
    bottomNavigationView.visibility= View.VISIBLE
}