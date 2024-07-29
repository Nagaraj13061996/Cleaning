package com.example.evo.trialapplication.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.evo.trialapplication.R
import com.example.evo.trialapplication.database.*
import com.example.evo.trialapplication.databinding.AddNewItemBinding
import com.example.evo.trialapplication.interview.TitleAdapter
import com.example.evo.trialapplication.util.showToast
import kotlinx.coroutines.launch


class AddNewItem : DialogFragment(R.layout.add_new_item) {

    private lateinit var binding:AddNewItemBinding
    private lateinit var productViewModel: ProductViewModel

    companion object{
        private val TAG = AddNewItem::class.java.simpleName

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=AddNewItemBinding.bind(view)
        val width = (resources.displayMetrics.widthPixels * 1).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.50).toInt()
        dialog?.window?.setLayout(width, height)
        val studentRepository = ProductRepository(ProductDataBase.getDatabase(requireContext()))
        val factory1 = ProductFactory(studentRepository)
        productViewModel = ViewModelProvider(this, factory1)[ProductViewModel::class.java]

        saveNewItem()
    }

    private fun saveNewItem() {
        binding.save.setOnClickListener {
            requireContext().showToast("Successfully ")
            Log.i(TAG, "addProduct:Clicked ")

            binding.apply {
                when {
                    product.text.toString().isEmpty() -> {
                        requireContext().showToast("Add Product")

                    }



                    else -> {
                        lifecycleScope.launch {

                            try {
                                productViewModel.addNewItem(
                                    AddNewItem(
                                        product.text.toString(),

                                        )
                                )
                                requireContext().showToast("save Successfully ")
                                product.setText("")

                                dialog?.dismiss()

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
}