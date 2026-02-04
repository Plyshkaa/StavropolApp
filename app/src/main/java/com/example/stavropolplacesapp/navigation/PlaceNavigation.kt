package com.example.stavropolplacesapp.navigation

import android.content.Context
import android.content.Intent
import com.example.stavropolplacesapp.Constants
import com.example.stavropolplacesapp.places.Place
import com.example.stavropolplacesapp.places.PlaceDetailActivity

object PlaceNavigation {
    fun openPlaceDetails(context: Context, place: Place) {
        // legacy fallback for old flows
        val intent = Intent(context, PlaceDetailActivity::class.java).apply {
            putExtra(Constants.EXTRA_NAME, place.name)
            putExtra(Constants.EXTRA_DESCRIPTION, place.description)
            putExtra(Constants.EXTRA_FULL_DESCRIPTION, place.fullDescription)
            putExtra(Constants.EXTRA_IMAGE_URL, place.imageUrl)
            putExtra(Constants.EXTRA_LATITUDE, place.latitude)
            putExtra(Constants.EXTRA_LONGITUDE, place.longitude)
            putExtra(Constants.EXTRA_COORDINATES, "${place.latitude}°N ${place.longitude}°E")
        }
        context.startActivity(intent)
    }
}
