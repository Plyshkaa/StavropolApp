package com.example.stavropolplacesapp.presentation.places

import com.example.stavropolplacesapp.core.BaseViewModel
import com.example.stavropolplacesapp.core.UiState
import com.example.stavropolplacesapp.data.repository.PlacesRepository
import com.example.stavropolplacesapp.data.repository.PlacesRepositoryImpl
import com.example.stavropolplacesapp.places.Place
import com.example.stavropolplacesapp.StavropolApp

class PlacesViewModel : BaseViewModel<PlacesState, PlacesEvent>() {
    
    private val placesRepository = PlacesRepositoryImpl(StavropolApp.instance)

    override fun createInitialState(): PlacesState = PlacesState()

    init {
        loadPlaces()
    }

    override fun onEvent(event: PlacesEvent) {
        when (event) {
            is PlacesEvent.LoadPlaces -> loadPlaces()
            is PlacesEvent.ToggleFavorite -> toggleFavorite(event.placeId)
            is PlacesEvent.NavigateToPlace -> {
                // Navigation will be handled by the UI
            }
            is PlacesEvent.Refresh -> loadPlaces()
        }
    }

    private fun loadPlaces() {
        setState { copy(placesState = UiState.Loading) }
        
        launch {
            try {
                val places = placesRepository.getPlaces()
                
                // Update places with favorite status
                val placesWithFavorites = places.map { place ->
                    place.copy(isFavorite = placesRepository.isFavorite(place.id))
                }
                
                setState { 
                    copy(
                        placesState = UiState.Success(placesWithFavorites)
                    )
                }
            } catch (e: Exception) {
                setState { 
                    copy(placesState = UiState.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }

    private fun toggleFavorite(placeId: Int) {
        launch {
            try {
                if (placesRepository.isFavorite(placeId)) {
                    placesRepository.removeFromFavorites(placeId)
                } else {
                    placesRepository.addToFavorites(placeId)
                }
                
                // Reload places to update favorite status
                loadPlaces()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

data class PlacesState(
    val placesState: UiState<List<Place>> = UiState.Loading
)

sealed class PlacesEvent {
    object LoadPlaces : PlacesEvent()
    object Refresh : PlacesEvent()
    data class ToggleFavorite(val placeId: Int) : PlacesEvent()
    data class NavigateToPlace(val place: Place) : PlacesEvent()
}
