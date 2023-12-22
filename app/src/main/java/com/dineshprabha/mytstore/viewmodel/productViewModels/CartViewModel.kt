package com.dineshprabha.mytstore.viewmodel.productViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dineshprabha.mytstore.data.Cartproduct
import com.dineshprabha.mytstore.firebase.FirebaseCommon
import com.dineshprabha.mytstore.helper.getProductPrice
import com.dineshprabha.mytstore.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {

    private val _cartProducts = MutableStateFlow<Resource<List<Cartproduct>>>(Resource.Unspecified())
    val cartProducts = _cartProducts.asStateFlow()

    private val _deleteDialog = MutableSharedFlow<Cartproduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()

    private var cartProductDocuments = emptyList<DocumentSnapshot>()

    val productPrice = cartProducts.map {
        when(it){
            is Resource.Success -> {
                calculatePrice(it.data!!)
            }
            else -> Unit
        }
    }

    fun deleteCartProduct(cartProduct : Cartproduct){
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1){
            val documentId = cartProductDocuments[index].id
            fireStore.collection("user").document(auth.uid!!).collection("cart")
                .document(documentId).delete()
        }

    }

    private fun calculatePrice(data: List<Cartproduct>): Any {
        return data.sumByDouble {cartproduct ->
            (cartproduct.product.offerPercentage.getProductPrice(cartproduct.product.price) * cartproduct.quantity).toDouble()
        }.toFloat()
    }

    init {
        getCartProducts()
    }

    private fun getCartProducts(){
        viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
        fireStore.collection("user").document(auth.uid!!).collection("cart")
            .addSnapshotListener { value, error ->
                if (error != null || value == null){
                    viewModelScope.launch { _cartProducts.emit(Resource.Error(error?.message.toString())) }

                }else{
                    cartProductDocuments = value.documents
                    val cartProducts = value.toObjects(Cartproduct::class.java)
                    viewModelScope.launch { _cartProducts.emit(Resource.Success(cartProducts)) }
                }
            }
    }

    fun changeQuantity(
        cartProduct: Cartproduct,
        quantityChanging : FirebaseCommon.QuantityChanging
    ){

        val index = cartProducts.value.data?.indexOf(cartProduct)

        /* Index could be equal to -1 if the [getCartProducts] delays which also delay the result we expect to be insdie the cart*/
        if (index != null && index != -1){
            val documentId = cartProductDocuments[index].id
            when(quantityChanging){
                FirebaseCommon.QuantityChanging.INCREASE -> {
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
                    increaseQuantity(documentId)
                }
                FirebaseCommon.QuantityChanging.DECREASE -> {
                    if(cartProduct.quantity == 1){
                        viewModelScope.launch { _deleteDialog.emit(cartProduct) }
                        return
                    }
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
                    decreaseQuantity(documentId)
                }
                else -> Unit
            }
        }
    }

    private fun decreaseQuantity(documentId: String) {
        firebaseCommon.decreaseQuantity(documentId) { result, exception ->
            if (exception != null)
                viewModelScope.launch { _cartProducts.emit(Resource.Error(exception.message.toString())) }
        }
    }

    private fun increaseQuantity(documentId: String) {
        firebaseCommon.increaseQuantity(documentId) { result, exception ->
            if (exception != null)
                viewModelScope.launch { _cartProducts.emit(Resource.Error(exception.message.toString())) }
        }
    }


}