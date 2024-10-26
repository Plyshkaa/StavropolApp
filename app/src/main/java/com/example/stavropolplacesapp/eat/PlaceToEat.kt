package com.example.stavropolplacesapp.eat

import java.io.Serializable

data class PlaceToEat(
    val name: String,
    val description: String,
    val coordinates: Coordinates,
    val phone: String,
    val address: String,
    val working_hours: WorkingDays,
    val photos: List<String>,
    val distance: Float
) : Serializable

data class Coordinates(
    val latitude: Double,
    val longitude: Double
) : Serializable

data class WorkingDays(
    val monday: String,
    val tuesday: String,
    val wednesday: String,
    val thursday: String,
    val friday: String,
    val saturday: String,
    val sunday: String
) : Serializable
