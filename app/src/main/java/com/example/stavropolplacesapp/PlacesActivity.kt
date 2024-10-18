package com.example.stavropolplacesapp

import JsonUtils
import Place
import PlacesAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlacesActivity : AppCompatActivity() {

    private lateinit var placesRecyclerView: RecyclerView
    private lateinit var placesAdapter: PlacesAdapter
    private var placesList: List<Place> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)

        placesRecyclerView = findViewById(R.id.places_recycler_view)
        placesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Настраиваем адаптер для RecyclerView
        placesAdapter = PlacesAdapter(placesList)
        placesRecyclerView.adapter = placesAdapter

        // Загружаем данные
        loadPlaces()
    }

    private fun loadPlaces() {
        CoroutineScope(Dispatchers.IO).launch {
            val places = JsonUtils.loadPlacesFromJson(this@PlacesActivity)
            withContext(Dispatchers.Main) {
                if (places != null) {
                    placesList = places
                    placesAdapter.updateData(placesList)
                } else {
                    Toast.makeText(
                        this@PlacesActivity,
                        "Ошибка загрузки данных",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
