package com.example.stavropolplacesapp.places

import com.example.stavropolplacesapp.JsonUtils
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stavropolplacesapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlacesListActivity : AppCompatActivity() {

    private lateinit var placesRecyclerView: RecyclerView
    private lateinit var placesAdapter: PlacesAdapter
    private var placesList: List<Place> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_list)

        // Инициализируем RecyclerView
        placesRecyclerView = findViewById(R.id.places_recycler_view)
        placesAdapter = PlacesAdapter(showDescription = true)
        placesRecyclerView.layoutManager = LinearLayoutManager(this)
        placesRecyclerView.adapter = placesAdapter

        // Загружаем данные
        loadPlaces()
    }

    private fun loadPlaces() {
        CoroutineScope(Dispatchers.IO).launch {
            val places =
                JsonUtils.loadPlacesFromJson(this@PlacesListActivity) // Используем JsonUtils
            withContext(Dispatchers.Main) {
                if (places != null) {
                    placesList = places
                    placesAdapter.submitList(placesList)
                } else {
                    Toast.makeText(
                        this@PlacesListActivity,
                        "Ошибка загрузки данных",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
