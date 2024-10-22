package com.example.stavropolplacesapp.places

import JsonUtils
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
import com.example.stavropolplacesapp.MainActivity
import com.example.stavropolplacesapp.R
import com.example.stavropolplacesapp.about.AboutScreen
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
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

        // Устанавливаем обработчик для навигации
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Открываем экран "Главная"
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_places -> {
                    // Открываем экран "Места"
                    val intent = Intent(this, PlacesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_about -> {
                    // Открываем экран "О приложении"
                    val intent = Intent(this, AboutScreen::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        placesRecyclerView = findViewById(R.id.places_recycler_view)
        placesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Настраиваем адаптер для RecyclerView
        placesAdapter = PlacesAdapter(placesList)
        placesRecyclerView.adapter = placesAdapter

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
