package com.example.athlos.ui.models

data class FoodsResponse(
    val foods: FoodList
)

data class FoodList(
    val food: List<FoodItemApi>?
)
