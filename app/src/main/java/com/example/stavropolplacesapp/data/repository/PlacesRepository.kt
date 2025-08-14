package com.example.stavropolplacesapp.data.repository

import com.example.stavropolplacesapp.places.Place
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {
    suspend fun getPlaces(): List<Place>
    suspend fun getPlaceById(id: Int): Place?
    suspend fun getFavoritePlaces(): List<Place>
    suspend fun addToFavorites(placeId: Int)
    suspend fun removeFromFavorites(placeId: Int)
    suspend fun isFavorite(placeId: Int): Boolean
    fun getFavoritePlacesFlow(): Flow<List<Int>>
}
