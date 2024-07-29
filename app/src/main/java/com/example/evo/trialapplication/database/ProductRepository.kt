package com.example.evo.trialapplication.database

import com.example.evo.trialapplication.interview.CartListModel


class ProductRepository(private val productDataBase: ProductDataBase) {

     suspend fun insert(productData: ProductData) = productDataBase.productDao().insertProduct(productData)
     suspend fun addNewItem(addNewItem: AddNewItem) = productDataBase.productDao().addNewItem(addNewItem)
    fun getProductData()=productDataBase.productDao().getProductData()
    fun getNewItem()=productDataBase.productDao().getNewItem()

    suspend fun delete(productData: ProductData)= productDataBase.productDao().delete(productData)


     suspend fun insertCart(cartListModel: CartListModel) = productDataBase.productDao().insertCart(cartListModel)
     suspend fun updateCart(cartListModel: CartListModel) = productDataBase.productDao().updateCart(cartListModel)
    fun getCartData()=productDataBase.productDao().getCartData()

    suspend fun deleteCartItem(cartListModel: CartListModel)= productDataBase.productDao().deleteCartItem(cartListModel)


}