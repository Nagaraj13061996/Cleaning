package com.example.evo.trialapplication.interview

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

 class JsonData{
 }
fun loadJSONFromAsset(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use { it.readText() }
}
fun parseJSONToPersons(jsonData: String): List<Root> {
    return Json.decodeFromString<List<Root>>(jsonData)
}

data class List1 (
    var _id: String? = null,
    var name: ArrayList<String>? = null,
    var price:Int = 0,
    var sequence_number:Int = 0,
    var is_default_selected:Boolean = false,
    var specification_group_id: String? = null,
    var unique_id:Int = 0,
)
data class Root (
    var _id: String? = null,
    var name: ArrayList<String>? = null,
    var price :Int= 0,
    var item_taxes: ArrayList<Int>? = null,
    var specifications: ArrayList<Specification>? = null,
    )
data class Specification (
    var _id: String? = null,
    var name: ArrayList<String>? = null,
    var sequence_number:Int = 0,
    var unique_id :Int= 0,
    var list: ArrayList<List1>? = null,
    var max_range:Int = 0,
    var range :Int= 0,
    var type:Int = 0,
    var is_required:Boolean = false,
    var isParentAssociate:Boolean = false,
    var modifierId: String? = null,
    var modifierGroupId: String? = null,
    var modifierGroupName: String? = null,
    var modifierName: String? = null,
    var isAssociated:Boolean = false,
    var user_can_add_specification_quantity:Boolean = false
)


