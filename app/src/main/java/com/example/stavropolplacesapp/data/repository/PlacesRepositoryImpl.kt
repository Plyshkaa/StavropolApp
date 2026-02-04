package com.example.stavropolplacesapp.data.repository

import android.content.Context
import com.example.stavropolplacesapp.JsonUtils
import com.example.stavropolplacesapp.favorites.FavoritePlacesStore
import com.example.stavropolplacesapp.places.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class PlacesRepositoryImpl(
    private val context: Context
) : PlacesRepository {

    override suspend fun getPlaces(): List<Place> {
        return JsonUtils.loadPlacesFromJson(context) ?: emptyList()
    }

    override suspend fun getPlaceById(id: Int): Place? {
        return getPlaces().find { it.id == id }
    }

    override suspend fun getFavoritePlaces(): List<Place> {
        val allPlaces = getPlaces()
        val favoriteIds = FavoritePlacesStore.getAllFavoriteIds(context)
        return allPlaces.filter { favoriteIds.contains(it.id) }
    }

    override suspend fun addToFavorites(placeId: Int) {
        FavoritePlacesStore.addFavorite(context, placeId)
    }

    override suspend fun removeFromFavorites(placeId: Int) {
        FavoritePlacesStore.removeFavorite(context, placeId)
    }

    override suspend fun isFavorite(placeId: Int): Boolean {
        return FavoritePlacesStore.isFavorite(context, placeId)
    }

    override fun getFavoritePlacesFlow(): Flow<List<Int>> = flow {
        val favoriteIds = FavoritePlacesStore.getAllFavoriteIds(context)
        emit(favoriteIds.toList())
    }
}
