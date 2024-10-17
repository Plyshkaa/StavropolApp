package com.example.stavropolplacesapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
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

        // Получаем данные из интента
        val imageUrl = intent.getStringExtra("imageUrl")
        val description = intent.getStringExtra("description")
        val placeLatitude = intent.getDoubleExtra("latitude", 0.0)
        val placeLongitude = intent.getDoubleExtra("longitude", 0.0)

        // Привязка View
        val placeImageView: ImageView = findViewById(R.id.placeImageView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val distanceTextView: TextView = findViewById(R.id.distanceTextView)

        // Установка изображения и описания
        Glide.with(this).load(imageUrl).into(placeImageView)
        descriptionTextView.text = description

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
            getCurrentLocationAndCalculateDistance(placeLatitude, placeLongitude, distanceTextView)
        }
    }

    private fun getCurrentLocationAndCalculateDistance(
        placeLatitude: Double,
        placeLongitude: Double,
        distanceTextView: TextView
    ) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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

// Форматируем результат: если дробной части нет, показываем целое число
                    // Вычисляем расстояние и устанавливаем его в TextView
                    val distance = userLocation.distanceTo(placeLocation) / 1000 // в км

// Округляем до целого числа
                    val roundedDistance = distance.toInt()

// Устанавливаем округленное значение
                    distanceTextView.text = "Расстояние до объекта: $roundedDistance км"

                }
            }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}

