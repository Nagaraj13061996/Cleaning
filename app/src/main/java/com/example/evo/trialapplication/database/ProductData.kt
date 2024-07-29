package com.example.evo.trialapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName ="product_table")
data class ProductData(
    var product:String="",
    var qty:String="",
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,


)

@Entity(tableName ="add_new_item_table")
data class AddNewItem(
    var product:String="",
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,


)
