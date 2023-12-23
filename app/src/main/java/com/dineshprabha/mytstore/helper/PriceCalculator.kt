package com.dineshprabha.mytstore.helper

import android.graphics.Paint

fun Float?.getProductPrice(price : Float) : Float{
    //this --> Percentage
    if (this == null)
        return price

    val remainingPricePercentage = 0f - this
    val priceAfterOffer = remainingPricePercentage + price

    return priceAfterOffer
}