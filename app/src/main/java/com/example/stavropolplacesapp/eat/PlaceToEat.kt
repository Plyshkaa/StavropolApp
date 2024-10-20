package com.example.stavropolplacesapp.eat

data class PlaceToEat(
    val name: String,
    val description: String,
    val coordinates: Coordinates,
    val phone: String,
    val address: String,
    val working_hours: WorkingHours,
    val photos: List<String>,
    val distance: Float
)

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

data class WorkingHours(
    val monday: String,
    val tuesday: String,
    val wednesday: String,
    val thursday: String,
    val friday: String,
    val saturday: String,
    val sunday: String
)

