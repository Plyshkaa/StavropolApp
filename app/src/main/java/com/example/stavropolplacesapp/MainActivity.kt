package com.example.stavropolplacesapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log // Добавляем импорт для логирования
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.graphics.Color
import android.view.View
import androidx.fragment.app.Fragment
import com.example.stavropolplacesapp.HomeFragment
import com.example.stavropolplacesapp.R
import com.example.stavropolplacesapp.about.AboutScreen
import com.example.stavropolplacesapp.about.SettingsFragment
import com.example.stavropolplacesapp.afisha.AfishaActivity
import com.example.stavropolplacesapp.eat.PlacesToEatActivity
import com.example.stavropolplacesapp.famous_people.ZemlyakiActivity
import com.example.stavropolplacesapp.places.PlacesActivity
import com.example.stavropolplacesapp.places.PlacesFragment
import com.example.stavropolplacesapp.region.RegionDetailActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    private lateinit var cardViewPlaces: CardView
    private lateinit var cardViewRegion: CardView
    private lateinit var cardViewFamousPeople: CardView
    private lateinit var imageView: ImageView

    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkLocationPermission()

        // Устанавливаем фрагмент по умолчанию при запуске активности
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        // Обрабатываем нажатия на элементы навигации
        bottomNavigationView.setOnItemSelectedListener { item ->
            Log.d("MainActivity", "Item clicked: ${item.title}")
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_places -> {
                    // Здесь открываем Activity для Мест вместо фрагмента
                    val intent = Intent(this, PlacesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_about -> {
                    // Здесь открываем Activity для экрана "О приложении" вместо фрагмента
                    val intent = Intent(this, AboutScreen::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }


        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        window.statusBarColor = Color.TRANSPARENT

        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Initialize views
        cardViewPlaces = findViewById(R.id.card_view_places)
        cardViewFamousPeople = findViewById(R.id.card_view_famous_people)
        imageView = findViewById(R.id.main_image)

        // Set up navigation between activities
        cardViewPlaces.setOnClickListener {
            Log.d("MainActivity", "Нажата карточка Места")
            val intent = Intent(this, PlacesActivity::class.java)
            startActivity(intent)
        }

        cardViewFamousPeople.setOnClickListener {
            Log.d("MainActivity", "Нажата карточка Земляки")
            val intent = Intent(this, ZemlyakiActivity::class.java)
            startActivity(intent)
        }

        // Card for Region
        val cardRegion = findViewById<CardView>(R.id.card_view_region)
        cardRegion.setOnClickListener {
            Log.d("MainActivity", "Нажата карточка Регион")
            val intent = Intent(this, RegionDetailActivity::class.java)
            startActivity(intent)
        }

        // Card for Afisha
        val afishaCardView: CardView = findViewById(R.id.card_view_afisha)
        afishaCardView.setOnClickListener {
            Log.d("MainActivity", "Нажата карточка Афиша")
            val intent = Intent(this, AfishaActivity::class.java)
            startActivity(intent)
        }

        // В вашем MainActivity, при нажатии на элемент интерфейса
        val cardViewWhereToEat: CardView = findViewById(R.id.card_view_where_to_eat)
        cardViewWhereToEat.setOnClickListener {
            Log.d("MainActivity", "Нажата карточка Где поесть")
            val intent = Intent(this, PlacesToEatActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkLocationPermission() {
        val preferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val isPermissionRequested = preferences.getBoolean("isPermissionRequested", false)

        if (!isPermissionRequested) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("MainActivity", "Запрос разрешения на доступ к местоположению")

                // Запрос разрешения
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )

                // Сохраняем, что запрос разрешения был выполнен
                preferences.edit().putBoolean("isPermissionRequested", true).apply()
            } else {
                onPermissionGranted()
            }
        } else {
            onPermissionGranted()
        }
    }

    private fun onPermissionGranted() {
        Log.d("MainActivity", "Разрешение на доступ к местоположению получено")
        // Логика для работы с местоположением, если разрешение уже есть
    }

    // Функция для замены фрагментов
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(
                    "MainActivity",
                    "Пользователь предоставил разрешение на доступ к местоположению"
                )
                onPermissionGranted()
            } else {
                Log.d("MainActivity", "Пользователь отклонил запрос на доступ к местоположению")
            }
        }
    }
}
