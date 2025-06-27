package com.example.athlos.ui.models

data class FoodItem(
    val id: String,
    val name: String,
    val quantity: Float,
    val unit: String,
    val protein: Float,
    val carbohydrate: Float,
    val fiber: Float,
    val fat: Float,
    val calories: Int
)