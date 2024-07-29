package com.example.evo.trialapplication.interview

import androidx.room.Entity
import androidx.room.PrimaryKey

data class CheckBoxModel(
    var title:String="",
    var subTitle:String="",
    var groupId:String="-1",
    var checkBoxList:MutableList<CheckList>
)
data class CheckList(
    var viewType:Int=-1,
    var itemCount:Int=0,
    var itemId:Int=0,
    var selected:Boolean=false,
    var name:String="",
    var price:Double=0.0,
    var groupId:String="-1",
)
data class CalculateModel(
    var groupId: String ="-1",
    var price: Double =0.0,
)
data class SelectedItemModel(
    var groupId: String ="-1",
    var itemId: Int =-1,
    var product: String ="",
    var itemCount: Int =0,
    var price: Double =0.0,
)
@Entity(tableName ="cart_list_table")
data class CartListModel(

    var product: String ="",
    var itemCount: Int =0,
    var price: Double =0.0,
    var viewPrice: Double =0.0,
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
)


