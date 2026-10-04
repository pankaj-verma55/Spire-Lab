package com.example.spirelab_pankajverma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spirelab_pankajverma.data.repository.ProductRepository
import com.example.spirelab_pankajverma.data.utility.NetworkObserver

class ProductViewModelFactory(private val repository: ProductRepository,
                              private val networkObserver: NetworkObserver): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(repository,networkObserver) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}