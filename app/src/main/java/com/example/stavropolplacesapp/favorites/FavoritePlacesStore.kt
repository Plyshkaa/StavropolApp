package com.example.stavropolplacesapp.favorites

import android.content.Context

object FavoritePlacesStore {
    private const val PREFS_NAME = "favorites_prefs"
    private const val KEY_IDS = "favorite_place_ids"

    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isFavorite(context: Context, placeId: Int): Boolean {
        val ids = getPrefs(context).getStringSet(KEY_IDS, emptySet()) ?: emptySet()
        return ids.contains(placeId.toString())
    }

    fun toggleFavorite(context: Context, placeId: Int): Boolean {
        val prefs = getPrefs(context)
        val current = prefs.getStringSet(KEY_IDS, emptySet())?.toMutableSet() ?: mutableSetOf()
        val idStr = placeId.toString()
        val nowFavorite = if (current.contains(idStr)) {
            current.remove(idStr)
            false
        } else {
            current.add(idStr)
            true
        }
        prefs.edit().putStringSet(KEY_IDS, current).apply()
        return nowFavorite
    }

    fun addFavorite(context: Context, placeId: Int) {
        val prefs = getPrefs(context)
        val current = prefs.getStringSet(KEY_IDS, emptySet())?.toMutableSet() ?: mutableSetOf()
        val idStr = placeId.toString()
        if (current.add(idStr)) {
            prefs.edit().putStringSet(KEY_IDS, current).apply()
        }
    }

    fun removeFavorite(context: Context, placeId: Int) {
        val prefs = getPrefs(context)
        val current = prefs.getStringSet(KEY_IDS, emptySet())?.toMutableSet() ?: mutableSetOf()
        val idStr = placeId.toString()
        if (current.remove(idStr)) {
            prefs.edit().putStringSet(KEY_IDS, current).apply()
        }
    }

    fun getAllFavoriteIds(context: Context): Set<Int> {
        val ids = getPrefs(context).getStringSet(KEY_IDS, emptySet()) ?: emptySet()
        return ids.mapNotNull { it.toIntOrNull() }.toSet()
    }
}

