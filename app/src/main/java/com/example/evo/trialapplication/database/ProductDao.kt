package com.example.evo.trialapplication.database

import androidx.room.*
import com.example.evo.trialapplication.interview.CartListModel
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductDao {

    @Insert
   suspend fun insertProduct(productData: ProductData)

   @Insert
   suspend fun addNewItem(addNewItem: AddNewItem)

    @Query("select * from product_table")
    fun getProductData(): Flow<List<ProductData>>

    @Query("select * from add_new_item_table")
    fun getNewItem(): Flow<List<AddNewItem>>

    @Delete
   suspend fun delete(productData: ProductData)


    @Insert
   suspend fun insertCart(cartListModel: CartListModel)

    @Update
   suspend fun updateCart(cartListModel: CartListModel)

    @Query("select * from cart_list_table")
    fun getCartData(): Flow<List<CartListModel>>

    @Delete
   suspend fun deleteCartItem(cartListModel: CartListModel)
}