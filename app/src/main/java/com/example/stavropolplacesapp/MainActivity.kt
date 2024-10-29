package com.example.stavropolplacesapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.stavropolplacesapp.R
import com.example.stavropolplacesapp.about.AboutScreen
import com.example.stavropolplacesapp.afisha.AfishaActivity
import com.example.stavropolplacesapp.eat.PlacesToEatActivity
import com.example.stavropolplacesapp.famous_people.ZemlyakiActivity
import com.example.stavropolplacesapp.places.PlacesActivity
import com.example.stavropolplacesapp.region.RegionDetailActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    private lateinit var cardViewPlaces: CardView
    private lateinit var cardViewRegion: CardView
    private lateinit var cardViewFamousPeople: CardView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Проверяем состояние геолокации и запрашиваем разрешение
        checkLocationServiceAndPermission()

        // Устанавливаем фрагмент по умолчанию при запуске активности
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        // Обрабатываем нажатия на элементы навигации
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_places -> {
                    startActivity(Intent(this, PlacesActivity::class.java))
                    true
                }
                R.id.nav_about -> {
                    startActivity(Intent(this, AboutScreen::class.java))
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

        setupCardListeners()
    }

    private fun checkLocationServiceAndPermission() {
        if (!isLocationEnabled()) {
            // Локация выключена, показываем диалог
            showLocationDialog()
        } else {
            // Локация включена, проверяем разрешения
            checkLocationPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showLocationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Включить геолокацию")
            .setMessage("Для работы приложения необходимо включить геолокацию.")
            .setPositiveButton("Включить") { dialog, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                Toast.makeText(this, "Геолокация не включена", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("MainActivity", "Запрос разрешения на доступ к местоположению")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            onPermissionGranted()
        }
    }

    private fun onPermissionGranted() {
        Log.d("MainActivity", "Разрешение на доступ к местоположению получено")
        // Логика для работы с местоположением, если разрешение уже есть
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted()
            } else {
                Log.d("MainActivity", "Пользователь отклонил запрос на доступ к местоположению")
                Toast.makeText(this, "Для работы приложения требуется разрешение на доступ к геолокации", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Функция для замены фрагментов
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setupCardListeners() {
        cardViewPlaces = findViewById(R.id.card_view_places)
        cardViewFamousPeople = findViewById(R.id.card_view_famous_people)
        imageView = findViewById(R.id.main_image)

        cardViewPlaces.setOnClickListener {
            startActivity(Intent(this, PlacesActivity::class.java))
        }

        cardViewFamousPeople.setOnClickListener {
            startActivity(Intent(this, ZemlyakiActivity::class.java))
        }

        val cardRegion = findViewById<CardView>(R.id.card_view_region)
        cardRegion.setOnClickListener {
            startActivity(Intent(this, RegionDetailActivity::class.java))
        }

        val afishaCardView: CardView = findViewById(R.id.card_view_afisha)
        afishaCardView.setOnClickListener {
            startActivity(Intent(this, AfishaActivity::class.java))
        }

        val cardViewWhereToEat: CardView = findViewById(R.id.card_view_where_to_eat)
        cardViewWhereToEat.setOnClickListener {
            startActivity(Intent(this, PlacesToEatActivity::class.java))
        }
    }
}
