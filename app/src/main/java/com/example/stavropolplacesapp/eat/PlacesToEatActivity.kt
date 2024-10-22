package com.example.stavropolplacesapp.eat

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stavropolplacesapp.MainActivity
import com.example.stavropolplacesapp.R
import com.example.stavropolplacesapp.about.AboutScreen
import com.example.stavropolplacesapp.places.PlacesActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class PlacesToEatActivity : AppCompatActivity() {

    private lateinit var placesRecyclerView: RecyclerView
    private lateinit var placesAdapter: PlacesToEatAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    private var currentLat: Double = 0.0
    private var currentLon: Double = 0.0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_to_eat)

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

        // Инициализируем RecyclerView
        placesRecyclerView = findViewById(R.id.places_to_eat_recycler_view)
        placesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Инициализация FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Получаем текущее местоположение пользователя
        getCurrentLocation()

        // Проверяем и запрашиваем разрешения на доступ к местоположению
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1000)
        } else {
            getLocationAndLoadPlaces()
        }

        // Настраиваем Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_places_to_eat)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Где поесть"
        // Добавляем кнопку назад
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.black)) // Устанавливаем чёрный цвет


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

    private fun getCurrentLocation() {
        try {
            val locationResult: Task<Location> = fusedLocationClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val location = task.result
                    currentLat = location.latitude
                    currentLon = location.longitude

                    // После получения местоположения загружаем места и передаем координаты
                    loadPlacesFromJson(currentLat, currentLon)
                } else {
                    Log.e("LocationError", "Не удалось получить текущее местоположение")
                }
            }
        } catch (e: SecurityException) {
            Log.e("LocationError", "Ошибка доступа к местоположению: ${e.message}")
        }
    }

    private fun getLocationAndLoadPlaces() {
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
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLat = location.latitude
                val currentLon = location.longitude

                // Загружаем данные из JSON и передаем их в адаптер вместе с текущими координатами
                loadPlacesFromJson(currentLat, currentLon)
            } else {
                Toast.makeText(this, "Не удалось получить местоположение", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Загружаем места из JSON и передаем координаты в адаптер
    private fun loadPlacesFromJson(lat: Double, lon: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val places = readJsonFile()
            withContext(Dispatchers.Main) {
                placesAdapter = PlacesToEatAdapter(places, lat, lon)
                placesRecyclerView.adapter = placesAdapter
            }
        }
    }

    private fun readJsonFile(): List<PlaceToEat> {
        val inputStream = assets.open("eat_place.json") // Открываем JSON из assets
        val reader = InputStreamReader(inputStream)
        val placeType = object : com.google.gson.reflect.TypeToken<List<PlaceToEat>>() {}.type
        return com.google.gson.Gson().fromJson(reader, placeType)
    }

    // Обработка запроса разрешений
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocationAndLoadPlaces()
        } else {
            Toast.makeText(this, "Разрешение на доступ к местоположению не предоставлено", Toast.LENGTH_SHORT).show()
        }
    }
}
