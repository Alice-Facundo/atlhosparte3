package com.example.athlos.ui.models

data class FatSecretSearchResponse(
    val foods: FoodsWrapper
)

data class FoodsWrapper(
    val food: List<FoodItemApi>
)

data class FoodItemApi(
    val food_id: String,
    val food_name: String,
    val food_type: String,
    val food_url: String,
    val brand_name: String?
)


data class FatSecretFoodDetailsResponse(
    val food: FoodDetailsWrapper
)

data class FoodDetailsWrapper(
    val food_id: String,
    val food_name: String,
    val servings: ServingsWrapper
)

data class ServingsWrapper(
    val serving: List<Serving>
)

data class Serving(
    val serving_id: String,
    val measure_description: String,
    val metric_amount: String,
    val metric_id: String,
    val calories: String,
    val carbohydrate: String,
    val protein: String,
    val fat: String,
    val sugar: String,
    val fiber: String,
)