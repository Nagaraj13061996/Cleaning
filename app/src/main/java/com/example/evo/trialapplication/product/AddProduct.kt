package com.example.evo.trialapplication.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.evo.trialapplication.R
import com.example.evo.trialapplication.database.*
import com.example.evo.trialapplication.databinding.AddProductBinding
import com.example.evo.trialapplication.interview.AddToCartDialog
import com.example.evo.trialapplication.util.showToast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AddProduct : Fragment(R.layout.add_product) {
    private lateinit var binding: AddProductBinding
    private lateinit var productViewModel:ProductViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=AddProductBinding.bind(view)

        val studentRepository = ProductRepository(ProductDataBase.getDatabase(requireContext()))
        val factory1 = ProductFactory(studentRepository)
        productViewModel = ViewModelProvider(this, factory1)[ProductViewModel::class.java]


        addProduct()
        listProduct()

        binding.addItem.setOnClickListener {
            AddNewItem().show(
                parentFragmentManager,
                AddNewItem::class.java.simpleName
            )

        }
    }

    private fun listProduct() {
        val productList: MutableList<String> = mutableListOf()
        lifecycleScope.launch {
            productViewModel.getNewItem().collect{_newItem->
                Log.i(TAG, "listProduct: $_newItem")
                productList.clear()
                for (i in _newItem){
                    productList.add(i.product)
                }
                val arrayAdapter =
                    ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.select_dialog_item_material,
                        productList
                    )

                binding.product.setAdapter(arrayAdapter)
                binding.product.threshold = 1
                binding.product.onItemClickListener = AdapterView.OnItemClickListener{ _, _, _, _ ->
                    for (i in 0 until productList.size) {
                        if (binding.product.text.toString() == productList[i]) {

//                    driverId = driverIdList[i]

                        }
                    }
                }
            }
        }


    }

    private fun addProduct() {
        Log.i(TAG, "addProduct:method ")
        binding.save.setOnClickListener {
            requireContext().showToast("Successfully ")
            Log.i(TAG, "addProduct:Clicked ")

            binding.apply {
                when {
                    product.text.toString().isEmpty() -> {
                        requireContext().showToast("Add Product")
                        Log.i(TAG, "addProduct:Add Product ")

                    }
                    qty.text.toString().isEmpty()->{
                        requireContext().showToast("Add Qty")
                        Log.i(TAG, "addProduct:Add Qty ")


                    }



                    else -> {
                        lifecycleScope.launch {

                            try {
                                productViewModel.insertProduct(
                                    ProductData(
                                        product.text.toString(),
                                        qty.text.toString(),

                                        )
                                )
                                requireContext().showToast("save Successfully ")
                                product.setText("")
                                qty.setText("")


                                val inputMethodManager = requireContext().getSystemService(
                                    AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)


                            } catch (e: NullPointerException) {
                                e.printStackTrace()
                            }

                        }
                    }
                }
            }
        }

    }
    companion object {
        private val TAG = AddProduct::class.java.simpleName
    }


}