package com.example.stavropolplacesapp

import JsonUtils
import Place
import PlacesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlacesListFragment : Fragment() {

    private lateinit var placesRecyclerView: RecyclerView
    private lateinit var placesAdapter: PlacesAdapter
    private var placesList: List<Place> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Подключаем layout для фрагмента
        val view = inflater.inflate(R.layout.fragment_places_list, container, false)
        placesRecyclerView = view.findViewById(R.id.places_recycler_view)
        placesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Настраиваем адаптер для RecyclerView
        placesAdapter = PlacesAdapter(placesList)
        placesRecyclerView.adapter = placesAdapter

        // Загружаем данные
        loadPlaces()

        return view
    }

    private fun loadPlaces() {
        CoroutineScope(Dispatchers.IO).launch {
            val places = JsonUtils.loadPlacesFromJson(requireContext())
            withContext(Dispatchers.Main) {
                if (places != null) {
                    placesList = places
                    placesAdapter.updateData(placesList)
                } else {
                    Toast.makeText(requireContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}



