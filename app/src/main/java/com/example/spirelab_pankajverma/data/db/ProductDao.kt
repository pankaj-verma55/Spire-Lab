package com.example.spirelab_pankajverma.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spirelab_pankajverma.data.item.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM product_cart_item")
    fun getCartItems(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: ProductEntity)

    @Query("SELECT * FROM product_cart_item WHERE productId = :id LIMIT 1")
    suspend fun getCartItemById(id: Int): ProductEntity?

    @Query("UPDATE product_cart_item SET count = :newCount WHERE productId = :productId")
    suspend fun updateCount(productId: Int, newCount: Int)

    @Query("DELETE FROM product_cart_item WHERE productId = :productId")
    suspend fun removeCartItem(productId: Int)

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM product_cart_item")
    fun getTotalCartCount(): Flow<Int>


    @Query("SELECT * FROM product_catalog_cache")
    suspend fun getAllCachedCatalog(): List<ProductCatalogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCatalog(products: List<ProductCatalogEntity>)
}