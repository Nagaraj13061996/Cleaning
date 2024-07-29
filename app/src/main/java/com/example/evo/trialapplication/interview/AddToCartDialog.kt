package com.example.evo.trialapplication.interview

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.evo.trialapplication.R
import com.example.evo.trialapplication.database.ProductDataBase
import com.example.evo.trialapplication.database.ProductFactory
import com.example.evo.trialapplication.database.ProductRepository
import com.example.evo.trialapplication.database.ProductViewModel
import com.example.evo.trialapplication.databinding.AddToCartDialogBinding
import com.example.evo.trialapplication.util.customSnackBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import java.util.*


class AddToCartDialog : DialogFragment(R.layout.add_to_cart_dialog) {
    private lateinit var binding: AddToCartDialogBinding
    private lateinit var viewModel: ProductViewModel
    private  var checkBoxData= mutableListOf <CheckBoxModel>()
    private var grandTotal=0.0
    private var finalTotal=0.0
    private var itemCount=1
    var calculateModelList:MutableList<CalculateModel> = mutableListOf()
    var selectedItemModelList:MutableList<SelectedItemModel> = mutableListOf()
    companion object {
        private val TAG = AddToCartDialog::class.java.simpleName
    }
    private lateinit var adapter: TitleAdapter

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddToCartDialogBinding.bind(view)
        val width = (resources.displayMetrics.widthPixels * 1).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.90).toInt()
        dialog?.window?.setLayout(width, height)
        adapter = TitleAdapter(requireContext())
        binding.recycle.adapter = adapter
        binding.recycle.layoutManager = LinearLayoutManager(context)
        val studentRepository = ProductRepository(ProductDataBase.getDatabase(requireContext()))
        val factory1 = ProductFactory(studentRepository)
        viewModel = ViewModelProvider(this, factory1)[ProductViewModel::class.java]

        setValuesInitial()
        setRecycle()
        addReduceProduct()
        saveDatabase()
        loadData("621da754abb8a52242c181d8","1 BHK")

    }

    private fun totalProductCalculate(_itemCount:Int) {
        binding.cartCount.text=_itemCount.toString()
        val subTotal= grandTotal*_itemCount
        finalTotal=subTotal
        binding.save.text=getString(R.string.add_to_order).plus("$subTotal")
    }

    private fun saveDatabase() {
        binding.save.setOnClickListener {

            val newModel: CartListModel
            val allStringValues =
                selectedItemModelList.joinToString(separator = " , ") { it.product }

            newModel = (CartListModel(
                allStringValues,
                binding.cartCount.text.toString().toInt(),
                grandTotal,
                finalTotal
            ))

            lifecycleScope.launch {
                viewModel.insertCart(newModel)
            }

            customSnackBar(requireView(), requireContext(), getString(R.string.update_items))

            lifecycleScope.launch {
                delay(1000)
                dialog?.dismiss()
                findNavController().navigate(R.id.cartList)
            }

        }

    }

    private fun setValuesInitial() {
        calculateModelList.add(CalculateModel("621da754abb8a52242c181d8",999.0))
        selectedItemModelList.add(SelectedItemModel("621da754abb8a52242c181d8",1,"1 BHK"+"",1,999.00))
        grandTotal=999.0
        finalTotal=grandTotal
        binding.save.text=getString(R.string.add_to_order).plus("$grandTotal")
        binding.cartCount.text=itemCount.toString()
    }

    private fun addReduceProduct() {
        binding.add.setOnClickListener {
            itemCount++
            totalProductCalculate(itemCount)
        }
        binding.less.setOnClickListener {
            itemCount--
            if (itemCount>=1){
                totalProductCalculate(itemCount)
            }
        }

    }

    private fun setRecycle() {

        adapter.differ.submitList(checkBoxData)
        adapter.totalPriceListener = { _price, _groupId, _selectedItemData ,_itemId,_name->

            if (_groupId=="621da754abb8a52242c181d8"){
                loadData(_groupId,_name)
            }
            val index = calculateModelList.indexOfFirst { it.groupId == _groupId }
            val indexItem = selectedItemModelList.indexOfFirst { it.groupId == _groupId }
            if (indexItem == -1) {
                if (_selectedItemData.itemCount > 1) {
                    selectedItemModelList.add(
                        SelectedItemModel(
                            _groupId,
                            _selectedItemData.itemId,
                            _selectedItemData.name + "(X${_selectedItemData.itemCount})",
                            _selectedItemData.itemCount,
                            _selectedItemData.price
                        )
                    )

                } else {
                    selectedItemModelList.add(
                        SelectedItemModel(
                            _groupId,
                            _selectedItemData.itemId,
                            _selectedItemData.name,
                            _selectedItemData.itemCount,
                            _selectedItemData.price
                        )
                    )

                }

            } else {
                if (_selectedItemData.itemCount > 1) {
                    selectedItemModelList[indexItem] = SelectedItemModel(
                        _groupId,
                        _selectedItemData.itemId,
                        _selectedItemData.name + "(X${_selectedItemData.itemCount})",
                        _selectedItemData.itemCount,
                        _selectedItemData.price
                    )

                } else {
                    selectedItemModelList[indexItem] = SelectedItemModel(
                        _groupId,
                        _selectedItemData.itemId,
                        _selectedItemData.name,
                        _selectedItemData.itemCount,
                        _selectedItemData.price
                    )

                }

            }
            if (index == -1) {
                calculateModelList.add(CalculateModel(_groupId, _price))
            } else {
                calculateModelList[index] = (CalculateModel(_groupId, _price))
            }
            val subTotal = calculateModelList.sumOf { it.price * itemCount }
            grandTotal = calculateModelList.sumOf { it.price }
            finalTotal = subTotal
            binding.save.text = getString(R.string.add_to_order).plus("$subTotal")
        }

        binding.dismiss.setOnClickListener {
            dialog?.dismiss()
        }
    }

private fun setData(_groupId: String, _name: String) {
    val checkBoxData = mutableListOf<CheckBoxModel>()

    val jsonData = loadJSONFromAsset(requireContext(), "data.json")

    val listType: Type = object : TypeToken<Root>() {}.type

    val gson = Gson()
    val persons: Root = gson.fromJson(jsonData, listType)
    checkBoxData.clear()

    for ((index, value) in (persons.specifications?.withIndex()!!)) {
        val checkList = mutableListOf<CheckList>()

        for (i in value.list!!) {

            if (value._id==_groupId){
                if (i.name?.get(0) == _name){
                    checkList.add(
                        CheckList(
                            value.type,
                            0,
                            i.sequence_number,
                            true,
                            i.name!!.get(0),
                            i.price.toDouble(),
                            i.specification_group_id!!
                        )
                    )
                }
                else{
                    checkList.add(
                        CheckList(
                            value.type,
                            0,
                            i.sequence_number,
                            false,
                            i.name!!.get(0),
                            i.price.toDouble(),
                            i.specification_group_id!!
                        )
                    )
                }

            }else{
                checkList.add(
                    CheckList(
                        value.type,
                        0,
                        i.sequence_number,
                        false,
                        i.name!!.get(0),
                        i.price.toDouble(),
                        i.specification_group_id!!
                    )
                )

            }

        }
        if (checkBoxData.size==0){
            checkBoxData.add(
                CheckBoxModel(
                    value.name?.get(0)!!,
                    "Choose 1",
                    value._id!!,
                    checkList
                )
            )

        }
        else{
            if (value.modifierName==_name){
                checkBoxData.add(
                    CheckBoxModel(
                        value.name?.get(0)!!,
                        "Choose 1",
                        value._id!!,
                        checkList
                    )
                )

            }

        }


    }

    adapter.differ.submitList(checkBoxData)
    selectedItemModelList.clear()
    calculateModelList.clear()

}
 private fun loadData(_groupId: String, _name: String) {
    val checkBoxData = mutableListOf<CheckBoxModel>()

    val jsonData = loadJSONFromAsset(requireContext(), "data.json")

    val listType: Type = object : TypeToken<Root>() {}.type

    val gson = Gson()
    val persons: Root = gson.fromJson(jsonData, listType)
    checkBoxData.clear()

    for ((index, value) in (persons.specifications?.withIndex()!!)) {
        val checkList = mutableListOf<CheckList>()

        for (i in value.list!!) {

            if (value._id==_groupId){
                if (i.name?.get(0) == _name){
                    checkList.add(
                        CheckList(
                            value.type,
                            0,
                            i.sequence_number,
                            true,
                            i.name!!.get(0),
                            i.price.toDouble(),
                            i.specification_group_id!!
                        )
                    )
                }
                else{
                    checkList.add(
                        CheckList(
                            value.type,
                            0,
                            i.sequence_number,
                            false,
                            i.name!!.get(0),
                            i.price.toDouble(),
                            i.specification_group_id!!
                        )
                    )
                }

            }else{
                checkList.add(
                    CheckList(
                        value.type,
                        0,
                        i.sequence_number,
                        false,
                        i.name!!.get(0),
                        i.price.toDouble(),
                        i.specification_group_id!!
                    )
                )

            }

        }
        if (checkBoxData.size==0){
            checkBoxData.add(
                CheckBoxModel(
                    value.name?.get(0)!!,
                    "Choose 1",
                    value._id!!,
                    checkList
                )
            )

        }
        else{
            if (value.modifierName==_name){
                checkBoxData.add(
                    CheckBoxModel(
                        value.name?.get(0)!!,
                        "Choose 1",
                        value._id!!,
                        checkList
                    )
                )

            }

        }


    }

    adapter.differ.submitList(checkBoxData)
    selectedItemModelList.clear()
    calculateModelList.clear()
     setValuesInitial()

}

}
