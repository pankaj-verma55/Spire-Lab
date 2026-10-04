package com.example.spirelab_pankajverma.data.repository

import android.util.Log
import com.example.spirelab_pankajverma.data.db.ProductDao
import com.example.spirelab_pankajverma.data.db.ProductEntity
import com.example.spirelab_pankajverma.data.item.Product
import com.example.spirelab_pankajverma.data.item.ProductDataItem
import com.example.spirelab_pankajverma.data.retrofit.ProductRetrofitApi
import com.example.spirelab_pankajverma.ui.screen.showBottomMessage
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val api: ProductRetrofitApi,
                        private val productDao: ProductDao) {

//    api
    suspend fun getProduct(): List<Product> {
        val response = api.productService.getProducts()
        if (response == null) {
            Log.d("SPIRE--->", "Response is null")
            return emptyList()
        }
        if(response.products.isEmpty()) {
            Log.d("SPIRE--->", "No products found")
            return emptyList()
        }
        return response.products
    }
// api
    suspend fun searchProduct(query: String): List<Product> {
        val response = api.productService.getSearchProduct(query)
        if (response == null) {
            Log.d("SPIRE--->", "Response is null")
            return emptyList()
        }
        if(response.products.isEmpty()) {
            Log.d("SPIRE--->", "No products found")
            return emptyList()
        }
        return response.products
    }

    // Add selected product to cart from db
//    suspend fun addToCart(product: Product) {
//        try {
//            val existingItem = productDao.getCartItemById(product.id)
//
//            if (existingItem != null) {
//                val newEntity = ProductEntity(
//                    productId = product.id,
//                    title = product.title ?: "",
//                    price = product.price,
//                    thumbnail = product.thumbnail,
//                    quantity = 1, // Start at 1
//                    images = product.images,
//                    description = product.description ?: "",
//                    rating = product.rating,
//                    category = product.category ?: "",
//                    brand = product.brand ?: "N/A",
//                    stock = product.stock
//                )
//                productDao.insertCartItem(newEntity)
//                Log.d("SPIRE--->", "Inserted new cart item: ${product.title}")
//            }
//        } catch (e: Exception) {
//            Log.e("SPIRE--->", "Failed to add to cart: ", e)
//        }
//    }

    suspend fun addToCart(product: Product) {
        try {
            val existingItem = productDao.getCartItemById(product.id)

            if (existingItem != null) {
                // Case 1: Item already in cart -> increment count if stock allows
                if (existingItem.count < existingItem.stock) {
                    val newCount = existingItem.count + 1
                    productDao.updateCount(product.id, newCount)
                    Log.d("SPIRE--->", "Incremented count to $newCount for: ${product.title}")
                } else {
                    Log.d("SPIRE--->", "Cannot add: Reached maximum stock for: ${product.title}")
                }
            } else {
                // Case 2: Item NOT in cart -> insert new item with count = 1
                val newEntity = ProductEntity(
                    productId = product.id,
                    title = product.title ?: "",
                    price = product.price,
                    thumbnail = product.thumbnail,
                    count = 1,
                    stock = product.stock,
                    images = product.images,
                    description = product.description ?: "",
                    rating = product.rating,
                    category = product.category ?: "",
                    brand = product.brand ?: "N/A",
                    quantity = 0
                )
                productDao.insertCartItem(newEntity)
                Log.d("SPIRE--->", "Inserted new cart item: ${product.title}")
            }
        } catch (e: Exception) {
            Log.e("SPIRE--->", "Failed to add to cart: ", e)
        }
    }

    // Get all cart items from Room db
    fun getCartItems(): Flow<List<ProductEntity>> {
        Log.d("SPIRE--->", "Product Get Cart Item: ${productDao.getCartItems()}")
        return productDao.getCartItems()
    }

    // Inside ProductRepository.kt
    suspend fun updateCount(productId: Int, newCount: Int) {
        try {
            productDao.updateCount(productId, newCount)
        } catch (e: Exception) {
            Log.e("SPIRE--->", "Error updating quantity: ", e)
        }
    }

    suspend fun removeFromCart(productId: Int) {
        try {
            productDao.removeCartItem(productId)
        } catch (e: Exception) {
            Log.e("SPIRE--->", "Error deleting cart item: ", e)
        }
    }
}