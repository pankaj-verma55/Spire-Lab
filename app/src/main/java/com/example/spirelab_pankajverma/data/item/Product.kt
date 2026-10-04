package com.example.spirelab_pankajverma.data.item

data class Product(
    val id: Int,
    val brand: String?,
    val category: String?,
    val description: String?,
    val images: List<String>,
    val price: Double,
    val rating: Double,
    val stock: Int,
    val thumbnail: String?,
    val title: String?,

    // Non useable field make it default
    val availabilityStatus: String = "",
    val dimensions: Dimensions? = null,
    val discountPercentage: Double = 0.0,
    val meta: Meta? = null,
    val minimumOrderQuantity: Int = 1,
    val returnPolicy: String = "",
    val reviews: List<Review> = emptyList(),
    val shippingInformation: String = "",
    val sku: String = "",
    val tags: List<String> = emptyList(),
    val warrantyInformation: String = "",
    val weight: Int = 0
)