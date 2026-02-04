package com.example.stavropolplacesapp.eat

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stavropolplacesapp.R
import com.example.stavropolplacesapp.Constants
import com.example.stavropolplacesapp.navigation.AppRoutes
import com.example.stavropolplacesapp.navigation.MainNavigation
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import java.io.InputStreamReader

class PlacesToEatActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var recyclerView: RecyclerView
    private lateinit var placesAdapter: PlacesToEatAdapter

    companion object {
        private const val LOCATION_REQUEST_CODE = 100
        private const val BACKGROUND_LOCATION_REQUEST_CODE = 101
        private const val TAG = "PlacesToEatActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_to_eat)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.menu.setGroupCheckable(0, false, true)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    MainNavigation.openRoute(this, AppRoutes.HOME)
                    true
                }
                R.id.nav_places -> {
                    MainNavigation.openRoute(this, AppRoutes.PLACES)
                    true
                }
                R.id.nav_favorites -> {
                    MainNavigation.openRoute(this, AppRoutes.FAVORITES)
                    true
                }
                R.id.nav_about -> {
                    MainNavigation.openRoute(this, AppRoutes.ABOUT)
                    true
                }
                else -> false
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        recyclerView = findViewById(R.id.places_to_eat_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar_places_to_eat)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Где поесть"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.black))
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        if (isGooglePlayServicesAvailable()) {
            checkLocationPermissions()
        } else {
            Toast.makeText(this, "Google Play Services недоступны на этом устройстве", Toast.LENGTH_LONG).show()
        }
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        return if (availability == ConnectionResult.SUCCESS) {
            true
        } else {
            GoogleApiAvailability.getInstance().getErrorDialog(this, availability, 0)?.show()
            false
        }
    }

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), BACKGROUND_LOCATION_REQUEST_CODE)
        }
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        Log.d(TAG, "Местоположение получено: ${location.latitude}, ${location.longitude}")
                        loadPlacesFromJson(location.latitude, location.longitude)
                    } else {
                        Log.d(TAG, "Местоположение null, запрашиваем обновление")
                        requestLocationUpdate()
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "Ошибка при получении местоположения", it)
                    showLocationError()
                }
        } else {
            showLocationError()
        }
    }

    private fun requestLocationUpdate() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
        }

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
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { updatedLocation ->
                        loadPlacesFromJson(updatedLocation.latitude, updatedLocation.longitude)
                    }
                    fusedLocationClient.removeLocationUpdates(this)
                }
            },
            Looper.getMainLooper()
        )
    }

    private fun loadPlacesFromJson(lat: Double, lon: Double) {
        Log.d(TAG, "Начало загрузки мест из JSON")
        CoroutineScope(Dispatchers.IO).launch {
            val places = readJsonFile()
            Log.d(TAG, "Загружено мест: ${places.size}")
            withContext(Dispatchers.Main) {
                if (places.isNotEmpty()) {
                    placesAdapter = PlacesToEatAdapter(places, lat, lon)
                    recyclerView.adapter = placesAdapter
                    Log.d(TAG, "Адаптер обновлен и данные загружены")
                } else {
                    Log.d(TAG, "Список мест пуст")
                }
            }
        }
    }

    private fun readJsonFile(): List<PlaceToEat> {
        val inputStream = assets.open("eat_place.json")
        val reader = InputStreamReader(inputStream)
        val placeType = object : TypeToken<List<PlaceToEat>>() {}.type
        return Gson().fromJson(reader, placeType)
    }

    private fun showLocationError() {
        Log.e(TAG, "Не удалось получить местоположение")
        Toast.makeText(this, "Не удалось получить местоположение", Toast.LENGTH_SHORT).show()
    }

    private fun showPermissionDeniedMessage() {
        Toast.makeText(this, "Для корректной работы необходимо разрешение на доступ к местоположению", Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    showPermissionDeniedMessage()
                }
            }
            BACKGROUND_LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Разрешение на фоновый доступ отклонено")
                }
            }
        }
    }
}
