package com.example.stavropolplacesapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.graphics.Color
import android.graphics.Paint
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class PlaceDetailActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        // Получаем координаты из интента
        val placeLatitude = intent.getDoubleExtra("latitude", 0.0)
        val placeLongitude = intent.getDoubleExtra("longitude", 0.0)

        // Привязываем TextView для координат
        val coordinatesTextView: TextView = findViewById(R.id.coordinatesTextView)
        coordinatesTextView.text = "Координаты: $placeLatitude, $placeLongitude"
        // Делаем текст синим и подчеркиваем его как гиперссылку
        coordinatesTextView.setTextColor(Color.BLUE)
        coordinatesTextView.paintFlags = coordinatesTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        // Делаем текст кликабельным
        coordinatesTextView.setOnClickListener {
            // Создаем Intent для открытия карты с координатами
            val uri = Uri.parse("geo:$placeLatitude,$placeLongitude?q=$placeLatitude,$placeLongitude(Место)")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)

            // Проверяем, есть ли приложение для открытия карт
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                // Если приложения для карт нет, открываем в браузере
                val browserUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$placeLatitude,$placeLongitude")
                val browserIntent = Intent(Intent.ACTION_VIEW, browserUri)
                startActivity(browserIntent)
            }
        }

        // Получаем данные из интента
        val imageUrl = intent.getStringExtra("imageUrl")
        val fullDescription = intent.getStringExtra("fullDescription")
        val coordinates = intent.getStringExtra("coordinates") // Извлекаем координаты


        // Привязка View
        val placeImageView: ImageView = findViewById(R.id.placeImageView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val distanceTextView: TextView = findViewById(R.id.distanceTextView)

        val timeTextView: TextView =
            findViewById(R.id.timeTextView) // Привязка TextView для координат

        // Установка изображения и описания
        Glide.with(this).load(imageUrl).into(placeImageView)
        descriptionTextView.text = fullDescription
        coordinatesTextView.text = coordinates

        // Инициализация FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Запрос разрешений на геолокацию
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getCurrentLocationAndCalculateDistance(
                placeLatitude,
                placeLongitude,
                distanceTextView,
                timeTextView
            )

        }
    }

    private fun getCurrentLocationAndCalculateDistance(
        placeLatitude: Double,
        placeLongitude: Double,
        distanceTextView: TextView,
        timeTextView: TextView
    ) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val userLocation = Location("userLocation").apply {
                        latitude = it.latitude
                        longitude = it.longitude
                    }

                    val placeLocation = Location("placeLocation").apply {
                        latitude = placeLatitude
                        longitude = placeLongitude
                    }

                    // Вычисляем расстояние и округляем его до целого числа
                    val distance = userLocation.distanceTo(placeLocation) / 1000 // в км
                    val roundedDistance = distance.toInt()

                    // Устанавливаем округленное значение в TextView
                    distanceTextView.text = "Расстояние до объекта: $roundedDistance км"
                    // Рассчитываем примерное время пребывания (50 км/ч)
                    val estimatedTime = if (roundedDistance > 0) (roundedDistance / 50.0 * 60).toInt() else 1

// Проверяем, больше ли время 60 минут
                    if (estimatedTime >= 60) {
                        val hours = estimatedTime / 60
                        val minutes = estimatedTime % 60
                        // Отображаем время в формате часы и минуты
                        timeTextView.text = if (minutes > 0) {
                            "Время до пребытия: $hours ч $minutes м"
                        } else {
                            "Время до пребытия: $hours ч"
                        }
                    } else {
                        // Отображаем время только в минутах
                        timeTextView.text = "Время до пребытия: $estimatedTime м"
                    }

                }
            }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
