package com.example.evo.trialapplication.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProductFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductViewModel(repository) as T
    }


}