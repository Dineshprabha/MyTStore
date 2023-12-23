package com.dineshprabha.mytstore.data.order

import com.dineshprabha.mytstore.data.Address
import com.dineshprabha.mytstore.data.Cartproduct

data class Order(
    val orderStatus: String,
    val totalPrice: Float,
    val products : List<Cartproduct>,
    val address: Address
)