package com.example.stavropolplacesapp.eat

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

object PlacesToEatRepository {
    fun loadPlaces(context: Context): List<PlaceToEat> {
        val inputStream = context.assets.open("eat_place.json")
        val reader = InputStreamReader(inputStream)
        val placeType = object : TypeToken<List<PlaceToEat>>() {}.type
        return Gson().fromJson(reader, placeType)
    }
}
