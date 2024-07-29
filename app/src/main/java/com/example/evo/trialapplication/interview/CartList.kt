package com.example.evo.trialapplication.interview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.evo.trialapplication.R
import com.example.evo.trialapplication.database.ProductDataBase
import com.example.evo.trialapplication.database.ProductFactory
import com.example.evo.trialapplication.database.ProductRepository
import com.example.evo.trialapplication.database.ProductViewModel
import com.example.evo.trialapplication.databinding.CartListBinding
import com.example.evo.trialapplication.util.customSnackBar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CartList : Fragment(R.layout.cart_list) {
    private lateinit var binding: CartListBinding
    var newModel: MutableList<CartListModel> = mutableListOf()

    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: CardListItem

    companion object {
        private val TAG = CartList::class.java.simpleName
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CartListBinding.bind(view)

        val studentRepository = ProductRepository(ProductDataBase.getDatabase(requireContext()))
        val factory1 = ProductFactory(studentRepository)
        viewModel = ViewModelProvider(this, factory1)[ProductViewModel::class.java]
        adapter = CardListItem()
        binding.cartListRecycle.adapter = adapter
        binding.cartListRecycle.layoutManager = LinearLayoutManager(context)

        setData()
        updatePriceListener()



    }

    private fun updateDataBase(itemCt:Int,update:CartListModel) {
        lifecycleScope.launch {
            val price = update.price * itemCt
            viewModel.updateCart(
                CartListModel(
                    update.product,
                    itemCt,
                    update.price,
                    price,
                    update.id
                )
            )
        }
        customSnackBar(
            requireView(), requireContext(), getString(R.string.update_items)
        )

    }

    private fun updatePriceListener() {
        adapter.selectedListener = { _index, _increaseDecrease ->

            if (_increaseDecrease) {
                var itemCt = newModel[_index].itemCount
                itemCt++
                updateDataBase(itemCt,newModel[_index])

            }

            else {
                var itemCt = newModel[_index].itemCount
                itemCt--
                if (itemCt >= 1) {
                    updateDataBase(itemCt,newModel[_index])
                }
                else {
                    lifecycleScope.launch {
                        viewModel.deleteCartItem(newModel[_index])
                        customSnackBar(
                            requireView(),
                            requireContext(),
                            getString(R.string.update_items)
                        )
                    }


                }

            }


        }

        adapter.deleteListener = { _deleteIndex ->
            lifecycleScope.launch {
                viewModel.deleteCartItem(newModel[_deleteIndex])
                customSnackBar(
                    requireView(), requireContext(), getString(R.string.update_items)
                )
            }
        }
    }

    private fun setData() {
        lifecycleScope.launch {
            viewModel.getCartData().collectLatest { _selectedData ->
                newModel = _selectedData.map { it.copy() }.toMutableList()
                adapter.addList(_selectedData)
                Log.i(TAG, "onViewCreated:$_selectedData ")
            }
        }

    }


}