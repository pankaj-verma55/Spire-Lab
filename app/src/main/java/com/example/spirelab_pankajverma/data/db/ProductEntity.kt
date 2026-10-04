package com.example.spirelab_pankajverma.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_cart_item")
data class ProductEntity(
    @PrimaryKey
    val productId: Int,
    val images: List<String>,
    val title: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val category: String,
    val brand: String,
    val stock: Int,
    val count: Int = 0,
    val thumbnail: String?,
    val quantity: Int
)
