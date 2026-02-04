package com.example.stavropolplacesapp.places

import com.example.stavropolplacesapp.JsonUtils
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stavropolplacesapp.R
import com.example.stavropolplacesapp.Constants
import com.example.stavropolplacesapp.navigation.AppRoutes
import com.example.stavropolplacesapp.navigation.MainNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.stavropolplacesapp.favorites.FavoritePlacesStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlacesActivity : AppCompatActivity() {

    private lateinit var placesRecyclerView: RecyclerView
    private lateinit var placesAdapter: PlacesAdapter
    private var placesList: List<Place> = listOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_list)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.selectedItemId = R.id.nav_places


        // Устанавливаем обработчик для навигации
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Открываем экран "Главная"
                    MainNavigation.openRoute(this, AppRoutes.HOME)
                    finish()
                    true
                }
                R.id.nav_places -> {
                    // Открываем экран "Места"
                    MainNavigation.openRoute(this, AppRoutes.PLACES)
                    finish()
                    true
                }
                R.id.nav_favorites -> {
                    MainNavigation.openRoute(this, AppRoutes.FAVORITES)
                    finish()
                    true
                }
                R.id.nav_about -> {
                    // Открываем экран "О приложении"
                    MainNavigation.openRoute(this, AppRoutes.ABOUT)
                    finish()
                    true
                }
                else -> false
            }
        }

        placesRecyclerView = findViewById(R.id.places_recycler_view)
        placesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Настраиваем адаптер для RecyclerView
        placesAdapter = PlacesAdapter()
        placesRecyclerView.adapter = placesAdapter
        
        // Обработчик изменения избранного
        placesAdapter.onFavoriteChanged = { placeId, isFavorite ->
            // Обновляем список при изменении избранного
            loadPlaces()
        }

        // Загружаем данные
        loadPlaces()

        // Настройка тулбара
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Убираем стандартное название приложения в Toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.black)) // Устанавливаем чёрный цвет
        // Настраиваем кастомный заголовок
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        toolbarTitle.text = "Места"  // Устанавливаем текст "Места" в тулбаре

        // Обработчик для кнопки назад
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Прозрачный статус-бар с видимыми иконками
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        // Используем светлый статус-бар для видимых иконок (черные иконки)
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    private fun loadPlaces() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val places = JsonUtils.loadPlacesFromJson(this@PlacesActivity)
                withContext(Dispatchers.Main) {
                                    if (places != null && places.isNotEmpty()) {
                    // Обновляем состояние избранного для каждого места
                    val placesWithFavorites = places.map { place ->
                        place.copy(isFavorite = FavoritePlacesStore.isFavorite(this@PlacesActivity, place.id))
                    }
                    placesList = placesWithFavorites
                    placesAdapter.submitList(placesList)
                } else {
                        // Показываем пустое состояние или ошибку
                        placesAdapter.submitList(emptyList())
                        Toast.makeText(
                            this@PlacesActivity,
                            "Нет данных для отображения",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    placesAdapter.submitList(emptyList())
                    Toast.makeText(
                        this@PlacesActivity,
                        "Ошибка загрузки данных: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
