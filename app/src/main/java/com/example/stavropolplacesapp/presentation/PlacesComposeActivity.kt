package com.example.stavropolplacesapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.stavropolplacesapp.presentation.places.PlacesScreen
import com.example.stavropolplacesapp.places.Place

class PlacesComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PlacesScreen(
                        onPlaceClick = { place ->
                            // TODO: Navigate to place details
                        }
                    )
                }
            }
        }
    }
}
