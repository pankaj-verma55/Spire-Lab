package com.example.spirelab_pankajverma.data.item

data class ProductDataItem(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)