package com.example.stavropolplacesapp.favorites

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stavropolplacesapp.JsonUtils
import com.example.stavropolplacesapp.R
import com.example.stavropolplacesapp.places.Place
import com.example.stavropolplacesapp.places.PlacesAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import com.example.stavropolplacesapp.navigation.AppRoutes
import com.example.stavropolplacesapp.navigation.MainNavigation

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlacesAdapter
    private var allPlaces: List<Place> = emptyList()
    private lateinit var emptyView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_list)

        recyclerView = findViewById(R.id.places_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PlacesAdapter(showDescription = false)
        adapter.onFavoriteChanged = { _, nowFavorite ->
            if (!nowFavorite) {
                renderFavorites()
            }
        }
        recyclerView.adapter = adapter
        emptyView = findViewById(R.id.empty_view)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.black))
        val title: TextView = findViewById(R.id.toolbar_title)
        title.text = "Избранное"

        toolbar.setNavigationOnClickListener { onBackPressed() }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        loadData()

        // Нижняя навигация
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.selectedItemId = R.id.nav_favorites
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    MainNavigation.openRoute(this, AppRoutes.HOME)
                    finish()
                    true
                }
                R.id.nav_places -> {
                    MainNavigation.openRoute(this, AppRoutes.PLACES)
                    finish()
                    true
                }
                R.id.nav_favorites -> true
                R.id.nav_about -> {
                    MainNavigation.openRoute(this, AppRoutes.ABOUT)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Обновляем список при возврате
        renderFavorites()
    }

    private fun loadData() {
        allPlaces = JsonUtils.loadPlacesFromJson(this) ?: emptyList()
        renderFavorites()
    }

    private fun renderFavorites() {
        val favIds = FavoritePlacesStore.getAllFavoriteIds(this)
        val favPlaces = allPlaces.filter { favIds.contains(it.id) }
        adapter.submitList(favPlaces)
        if (::emptyView.isInitialized) {
            emptyView.visibility = if (favPlaces.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}
