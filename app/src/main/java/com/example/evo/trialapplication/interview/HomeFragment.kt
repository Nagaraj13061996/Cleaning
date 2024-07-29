package com.example.evo.trialapplication.interview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.evo.trialapplication.R
import com.example.evo.trialapplication.database.ProductDataBase
import com.example.evo.trialapplication.database.ProductFactory
import com.example.evo.trialapplication.database.ProductRepository
import com.example.evo.trialapplication.database.ProductViewModel
import com.example.evo.trialapplication.databinding.AddDialogBinding
import com.example.evo.trialapplication.databinding.FragmentHome2Binding
import com.example.evo.trialapplication.util.customSnackBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.lang.reflect.Type


class HomeFragment : Fragment(R.layout.fragment_home2) {
    private lateinit var binding: FragmentHome2Binding
    private lateinit var alertDialog: androidx.appcompat.app.AlertDialog
    private lateinit var viewModel: ProductViewModel
    private lateinit var dialogBinding: AddDialogBinding
    private  var repeatLast=CartListModel()
    private var checkList= mutableListOf<CheckList>()
    var list=0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentHome2Binding.bind(view)

        val studentRepository = ProductRepository(ProductDataBase.getDatabase(requireContext()))
        val factory1 = ProductFactory(studentRepository)
        viewModel = ViewModelProvider(this, factory1)[ProductViewModel::class.java]

        setDialog()
        increaseProduct()
        decreaseProduct()
        setCartCount()
        addProductDialog()






        }


    private fun updateData(_itemCount: Int, update: CartListModel) {
        lifecycleScope.launch {
            val price = update.price * _itemCount
            viewModel.updateCart(
                (CartListModel(
                    update.product, _itemCount, update.price, price, update.id
                ))
            )
        }
        alertDialog.dismiss()

        customSnackBar(requireView(), requireContext(), getString(R.string.update_items))
        setCartCount()
    }

    private fun setDialog() {
        alertDialog = androidx.appcompat.app.AlertDialog.Builder(requireContext()).create()
        val layout = layoutInflater.inflate(R.layout.add_dialog, null)
        dialogBinding = AddDialogBinding.bind(layout)
        alertDialog.setView(layout)
    }

    private fun addProductDialog() {
        binding.custom.setOnClickListener {
            AddToCartDialog().show(
                parentFragmentManager,
                AddToCartDialog::class.java.simpleName
            )

        }

    }



    private fun setCartCount() {
        lifecycleScope.launch {
            viewModel.getCartData().collect{_listCart->
                list=_listCart.size
                if (list>0){
                    val itemCountView =  _listCart.sumOf { it.itemCount }
                    repeatLast= _listCart[list-1]
                    binding.cartCount.text=itemCountView.toString()
                }
                if (list==0){
                    binding.linear3.isVisible=false
                    binding.custom.isVisible=true
                }else{
                    binding.linear3.isVisible=true
                    binding.custom.isVisible=false
                }


            }
        }

    }
    private fun increaseProduct() {
        binding.add.setOnClickListener {

            dialogBinding.apply {
                title.text=getString(R.string.repeat_last)
                content.text=repeatLast.product
               okBtn.isVisible=false
                repeatBtn.isVisible = true
                customBtn.isVisible = true
                custom.isVisible = true

                repeatBtn.setOnClickListener {
                    var itemCt=repeatLast.itemCount
                    itemCt++
                    updateData(itemCt,repeatLast)
                }
            }

            dialogBinding.customBtn.setOnClickListener {
                alertDialog.dismiss()
                AddToCartDialog().show(
                    parentFragmentManager,
                    AddToCartDialog::class.java.simpleName
                )

            }
            dialogBinding.dismiss.setOnClickListener {
                alertDialog.dismiss()
            }


            alertDialog.show()
        }

    }
    private fun decreaseProduct() {
        binding.less.setOnClickListener {
            if (list == 1) {
                lifecycleScope.launch {
                    if (repeatLast.itemCount > 1) {
                        var itemCount = repeatLast.itemCount
                        itemCount--
                        updateData(itemCount,repeatLast)

                    } else {
                        viewModel.deleteCartItem(repeatLast)
                        customSnackBar(requireView(), requireContext(), getString(R.string.cart_clear))
                        setCartCount()
                    }

                }
            }

            else {
                dialogBinding.apply {
                    title.text = getString(R.string.attention)
                    content.text = getString(R.string.remove_correct_item)
                    okBtn.isVisible = true
                    repeatBtn.isVisible = false
                    customBtn.isVisible = false
                    custom.isVisible = false

                   dismiss.setOnClickListener {
                        alertDialog.dismiss()
                    }
                    okBtn.setOnClickListener {
                        alertDialog.dismiss()
                    }
                }


                alertDialog.show()

            }

        }

    }



}