package com.dineshprabha.mytstore.data

sealed class Category (val category: String){

    object Chair : Category("Chair")
    object Cupboadr : Category("Cupboadr")
    object Table : Category("Table")
    object Furniture : Category("Furniture")
}