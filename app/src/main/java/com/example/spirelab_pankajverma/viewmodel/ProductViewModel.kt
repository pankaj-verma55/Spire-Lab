package com.example.spirelab_pankajverma.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spirelab_pankajverma.data.db.ProductEntity
import com.example.spirelab_pankajverma.data.item.Product
import com.example.spirelab_pankajverma.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    // 1. Observable list from Room DB
    val dbCartItem: StateFlow<List<ProductEntity>> = repository.getCartItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 2. Real-time total count of all clicks/quantities
    val cartTotalCount: StateFlow<Int> = dbCartItem
        .map { list -> list.size } // Sum of all quantities added
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

//    private val _cartItem = MutableStateFlow<List<Product>>(emptyList())

//    private val _dbCartItem = MutableStateFlow<List<ProductEntity>>(emptyList())
//    val dbCartItem: StateFlow<List<ProductEntity>> = _dbCartItem.asStateFlow()

    private val _list = MutableStateFlow<List<Product>>(emptyList())
    val list: StateFlow<List<Product>> = _list.asStateFlow()

    private val _loader = MutableStateFlow(false)
    val loader: StateFlow<Boolean> = _loader

    init {
        observeSearch()
    }

    fun getProduct() {
        viewModelScope.launch {
            try {
                _loader.value = true
                val response = repository.getProduct()

                Log.d("SPIRE search--->", "API Response: $response")

                _list.value = response

            } catch (e: Exception) {
                _loader.value = false
                Log.e("SPIRE search--->", "API Error: ${e.message}", e)
            } finally {
                _loader.value = false
            }
        }
    }


    fun addToCart(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToCart(product)
        }
    }

    fun searchProduct(query: String) {
        viewModelScope.launch {
            _list.value = repository.searchProduct(query)
            Log.e("SPIRE search--->", "API get product search${_list.value}")

        }
    }

    fun observeSearch() {
        viewModelScope.launch {

            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { query ->
                    try {
                        _loader.value = true

                        if (query.isBlank()) {
                            _list.value = repository.getProduct()
                            Log.e("SPIRE search--->", "API get product observe${_list.value.size}")
                        } else {
                            _list.value = repository.searchProduct(query)
                            Log.e("SPIRE search--->", "API get search product observe${_list.value.size}")
                        }
                    } catch (e: Exception) {
                        Log.e(
                            "SPIRE search--->",
                            "API Error: ${e.message}",
                            e
                        )
                    } finally {
                        _loader.value = false
                    }
                }
        }
    }

    fun updateCount(productId: Int, newCount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCount(productId, newCount)
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFromCart(productId)
        }
    }
}