package com.example.evo.trialapplication.new_data

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.evo.trialapplication.R
import com.example.evo.trialapplication.databinding.NewHomeBinding
import com.example.evo.trialapplication.interview.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class NewHome : Fragment(R.layout.new_home) {
    private lateinit var adapter: TitleAdapter1

    private lateinit var binding: NewHomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = NewHomeBinding.bind(view)

        adapter = TitleAdapter1(requireContext())
        binding.recycle.adapter = adapter
        binding.recycle.layoutManager = LinearLayoutManager(context)





        setData()




    }

    private fun setData() {
         var checkBoxData = mutableListOf<CheckBoxModel>()

        val jsonData = loadJSONFromAsset(requireContext(), "data.json")

        val listType: Type = object : TypeToken<Root>() {}.type

        val gson = Gson()
        val persons: Root = gson.fromJson(jsonData, listType)
        for ((index, value) in (persons.specifications?.withIndex()!!)) {
            val checkList = mutableListOf<CheckList>()

            for (i in value.list!!) {

//                if (value.modifierName == "1 BHK")
                checkList.add(
                    CheckList(
                        value.type,
                        0,
                        i.sequence_number,
                        i.is_default_selected,
                        i.name!!.get(0),
                        i.price.toDouble(),
                        i.specification_group_id!!
                    )
                )
//                if (value.type==2)
//                    break
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

            }else{
                if (value.modifierName=="1 BHK"){
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
        adapter.totalPriceListener = { _price, _groupId, _selectedItemData ,_itemId,_name->

            if (_groupId=="621da754abb8a52242c181d8"){
                loadData(_groupId,_name)
            }

        }
    }

    private fun loadData(_groupId: String, _name: String) {
        var checkBoxData = mutableListOf<CheckBoxModel>()

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
//                if (value.type==2)
//                    break
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

            }else{
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
        Log.i("TAG", "onViewCreatedcheckBoxData:$checkBoxData ")

    }
}