package com.example.evo.trialapplication.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.evo.trialapplication.R
import com.example.evo.trialapplication.database.ProductDataBase
import com.example.evo.trialapplication.database.ProductFactory
import com.example.evo.trialapplication.database.ProductRepository
import com.example.evo.trialapplication.database.ProductViewModel
import com.example.evo.trialapplication.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch


class Home : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ProductViewAdapter
    private lateinit var productViewModel: ProductViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding=FragmentHomeBinding.bind(view)

        val studentRepository = ProductRepository(ProductDataBase.getDatabase(requireContext()))
        val factory1 = ProductFactory(studentRepository)
        productViewModel = ViewModelProvider(this, factory1)[ProductViewModel::class.java]

        getProductData()
    }

    private fun getProductData() {
        lifecycleScope.launch{
            productViewModel.getProductData().collect{_canvasData->

                val productCanvasView = binding.canvas
                productCanvasView.setProductList(_canvasData)
                Log.i(TAG, "getProductData:$_canvasData ")
                adapter = ProductViewAdapter()
                binding.recycle.adapter = adapter
                binding.recycle.layoutManager = LinearLayoutManager(requireContext())
                adapter.differ.submitList(_canvasData)
            }
        }
    }
    companion object {
        private val TAG = Home::class.java.simpleName
    }

}