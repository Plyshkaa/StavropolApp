package com.example.stavropolplacesapp.navigation

import android.content.Context
import android.content.Intent
import com.example.stavropolplacesapp.eat.PlaceEatDetailActivity
import com.example.stavropolplacesapp.eat.PlaceToEat
import com.google.gson.Gson

object EatNavigation {
    fun openPlaceEatDetails(context: Context, place: PlaceToEat) {
        val intent = Intent(context, PlaceEatDetailActivity::class.java).apply {
            putExtra(EatExtras.NAME, place.name)
            putExtra(EatExtras.DESCRIPTION, place.description)
            putExtra(EatExtras.ADDRESS, place.address)
            putExtra(EatExtras.PHONE, place.phone)
            putExtra(EatExtras.PHOTOS, place.photos.toTypedArray())
            putExtra(EatExtras.COORDINATES, "${place.coordinates.latitude}, ${place.coordinates.longitude}")
            val workingHoursJson = Gson().toJson(place.working_hours)
            putExtra(EatExtras.WORKING_HOURS, workingHoursJson)
        }
        context.startActivity(intent)
    }
}
