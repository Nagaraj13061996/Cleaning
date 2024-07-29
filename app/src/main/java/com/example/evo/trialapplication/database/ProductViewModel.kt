package com.example.evo.trialapplication.database


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evo.trialapplication.interview.CartListModel
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {


     fun insertProduct(productData: ProductData) = viewModelScope.launch {
        repository.insert(productData)
    }
     fun addNewItem(addNewItem: AddNewItem) = viewModelScope.launch {
        repository.addNewItem(addNewItem)
    }
    fun getProductData()=repository.getProductData()
    fun getNewItem()=repository.getNewItem()

   suspend  fun deleteProduct(productData: ProductData)=repository.delete(productData)


    suspend fun insertCart(cartListModel: CartListModel) = viewModelScope.launch {
        repository.insertCart(cartListModel)
    }
    suspend fun updateCart(cartListModel: CartListModel) = viewModelScope.launch {
        repository.updateCart(cartListModel)
    }
    fun getCartData()=repository.getCartData()

    suspend fun deleteCartItem(cartListModel: CartListModel)= repository.deleteCartItem(cartListModel)

}