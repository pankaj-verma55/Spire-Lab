package com.example.spirelab_pankajverma.data.repository

import android.content.Context
import android.util.Log
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.spirelab_pankajverma.data.db.ProductCatalogEntity
import com.example.spirelab_pankajverma.data.db.ProductDao
import com.example.spirelab_pankajverma.data.db.ProductEntity
import com.example.spirelab_pankajverma.data.item.Product
import com.example.spirelab_pankajverma.data.retrofit.ProductRetrofitApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductRepository(private val api: ProductRetrofitApi,
                        private val productDao: ProductDao,
                        private val context: Context
) {


    suspend fun getProductAll(): List<Product> {
        return try {
            val response = api.productService.getProducts()
            val remoteProducts = response?.products ?: emptyList()

            if (remoteProducts.isNotEmpty()) {
                // Map List<Product> -> List<ProductCatalogEntity> for Room
                val entities = remoteProducts.map { p ->
                    ProductCatalogEntity(
                        id = p.id,
                        title = p.title,
                        price = p.price,
                        thumbnail = p.thumbnail,
                        stock = p.stock,
                        images = p.images ?: emptyList(),
                        description = p.description,
                        rating = p.rating,
                        category = p.category,
                        brand = p.brand
                    )
                }
                productDao.insertAllCatalog(entities)
                prefetchImages(remoteProducts)
            }
            remoteProducts
        } catch (e: Exception) {
            Log.e("SPIRE--->", "Network error, reading from cache: ", e)
            // Map List<ProductCatalogEntity> -> List<Product> for UI
            productDao.getAllCachedCatalog().map { entity ->
                Product(
                    id = entity.id,
                    title = entity.title,
                    price = entity.price,
                    thumbnail = entity.thumbnail,
                    stock = entity.stock,
                    images = entity.images,
                    description = entity.description,
                    rating = entity.rating,
                    category = entity.category,
                    brand = entity.brand
                )
            }
        }
    }

//    api
    suspend fun getProductCart(): List<Product> {
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
    private fun prefetchImages(products: List<Product>) {
        CoroutineScope(Dispatchers.IO).launch {
            products.forEach { product ->
                val imageUrl = product.images.firstOrNull() ?: product.thumbnail
                if (!imageUrl.isNullOrBlank()) {
                    val request = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .networkCachePolicy(CachePolicy.ENABLED)
                        .build()
                    context.imageLoader.enqueue(request)
                }
            }
        }
    }
}