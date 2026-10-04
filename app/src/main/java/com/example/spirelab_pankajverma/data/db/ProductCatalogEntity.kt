package com.example.spirelab_pankajverma.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_catalog_cache")
data class ProductCatalogEntity(
    @PrimaryKey
    val id: Int,
    val title: String? = "",
    val price: Double = 0.0,
    val thumbnail: String? = null,
    val stock: Int = 0,
    val images: List<String> = emptyList(),
    val description: String? = "",
    val rating: Double = 0.0,
    val category: String? = "",
    val brand: String? = ""
)