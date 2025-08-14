package com.example.stavropolplacesapp

object Constants {
    
    // Request codes
    const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    
    // Intent extras
    const val EXTRA_NAME = "name"
    const val EXTRA_DESCRIPTION = "description"
    const val EXTRA_FULL_DESCRIPTION = "fullDescription"
    const val EXTRA_IMAGE_URL = "imageUrl"
    const val EXTRA_LATITUDE = "latitude"
    const val EXTRA_LONGITUDE = "longitude"
    const val EXTRA_COORDINATES = "coordinates"
    
    // File names
    const val PLACES_JSON_FILE = "places.json"
    const val EAT_PLACES_JSON_FILE = "eat_place.json"
    const val PEOPLE_JSON_FILE = "people.json"
    const val REGION_JSON_FILE = "region.json"
    
    // Log tags
    const val TAG_MAIN_ACTIVITY = "MainActivity"
    const val TAG_JSON_UTILS = "JsonUtils"
    const val TAG_PLACES_ADAPTER = "PlacesAdapter"
    
    // Animation durations
    const val ANIMATION_DURATION_SHORT = 200L
    const val ANIMATION_DURATION_MEDIUM = 300L
    const val ANIMATION_DURATION_LONG = 500L
    
    // UI constants
    const val CARD_CORNER_RADIUS = 12f
    const val CARD_ELEVATION = 4f
    
    // Network timeouts
    const val NETWORK_TIMEOUT = 30000L // 30 seconds
    
    // Cache sizes
    const val IMAGE_CACHE_SIZE = 50 * 1024 * 1024 // 50MB
    const val MEMORY_CACHE_SIZE = 20 * 1024 * 1024 // 20MB
}
