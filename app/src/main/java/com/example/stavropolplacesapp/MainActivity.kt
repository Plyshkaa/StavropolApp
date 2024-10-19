package com.example.stavropolplacesapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.graphics.Color
import android.view.View
import com.example.stavropolplacesapp.afisha.AfishaActivity
import com.example.stavropolplacesapp.famous_people.ZemlyakiActivity
import com.example.stavropolplacesapp.places.PlacesActivity
import com.example.stavropolplacesapp.region.RegionDetailActivity


class MainActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    private lateinit var cardViewPlaces: CardView
    private lateinit var cardViewRegion: CardView
    private lateinit var cardViewFamousPeople: CardView
    private lateinit var imageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkLocationPermission()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

// Устанавливаем цвет статус-бара как прозрачный
        window.statusBarColor = Color.TRANSPARENT

// Чтобы сделать иконки и текст в статус-баре видимыми, используем флаг SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        // Initialize views
        cardViewPlaces = findViewById(R.id.card_view_places)
        cardViewFamousPeople = findViewById(R.id.card_view_famous_people)
        imageView = findViewById(R.id.main_image)

        // Set up navigation between activities
        cardViewPlaces.setOnClickListener {
            val intent = Intent(this, PlacesActivity::class.java)
            startActivity(intent)
        }

        cardViewFamousPeople.setOnClickListener {
            val intent = Intent(this, ZemlyakiActivity::class.java)
            startActivity(intent)
        }

        // Card for Region
        val cardRegion = findViewById<CardView>(R.id.card_view_region)
        cardRegion.setOnClickListener {
            val intent = Intent(this, RegionDetailActivity::class.java)
            startActivity(intent)
        }

        // Card for Afisha
        val afishaCardView: CardView = findViewById(R.id.card_view_afisha)
        afishaCardView.setOnClickListener {
            val intent = Intent(this, AfishaActivity::class.java)
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
        // Логика для работы с местоположением, если разрешение уже есть
        // Например, обновление данных местоположения
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение получено
                onPermissionGranted()
            } else {
                // Обработка случая, если разрешение не было получено
            }
        }
    }
}
