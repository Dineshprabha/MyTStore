package com.dineshprabha.mytstore.data

sealed class Category (val category: String){

    object Chair : Category("Chair")
    object Cupboard : Category("Cupboard")
    object Table : Category("Table")
    object Furniture : Category("Furniture")
}