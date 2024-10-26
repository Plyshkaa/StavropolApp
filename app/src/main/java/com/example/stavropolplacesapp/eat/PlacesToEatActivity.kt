package com.example.stavropolplacesapp.eat

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stavropolplacesapp.MainActivity
import com.example.stavropolplacesapp.R
import com.example.stavropolplacesapp.about.AboutScreen
import com.example.stavropolplacesapp.places.PlacesActivity
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
        private const val TAG = "PlacesToEatActivity" // Для логирования
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_to_eat)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        // Сбрасываем подсветку всех иконок
        bottomNavigationView.menu.setGroupCheckable(0, false, true)

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

        // Инициализация клиента для получения местоположения
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Инициализация RecyclerView
        recyclerView = findViewById(R.id.places_to_eat_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Проверяем разрешения на доступ к местоположению
        checkLocationPermissions()

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

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Если разрешение не предоставлено, запрашиваем его
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        } else {
            // Разрешение предоставлено, получаем местоположение
            getLocation()
        }

        // Для Android 10 (Q) и выше необходимо дополнительное разрешение для фонового доступа к местоположению
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                // Запрашиваем фоновое разрешение
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), BACKGROUND_LOCATION_REQUEST_CODE)
            }
        }
    }

    private fun getLocation() {
        // Проверяем еще раз, есть ли разрешение на доступ к местоположению
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        // Успешно получили местоположение, загружаем данные о местах из JSON
                        Log.d(TAG, "Местоположение получено: ${location.latitude}, ${location.longitude}")
                        loadPlacesFromJson(location.latitude, location.longitude)
                    } else {
                        // Не удалось получить местоположение
                        showLocationError()
                    }
                }
                .addOnFailureListener {
                    // Обработка ошибки при получении местоположения
                    Log.e(TAG, "Ошибка при получении местоположения", it)
                    showLocationError()
                }
        } else {
            // Разрешение не предоставлено, показываем ошибку
            showLocationError()
        }
    }

    // Загружаем места из JSON и передаем координаты в адаптер
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


    // Чтение данных из JSON-файла, расположенного в assets
    private fun readJsonFile(): List<PlaceToEat> {
        val inputStream = assets.open("eat_place.json") // Открываем JSON из assets
        val reader = InputStreamReader(inputStream)
        val placeType = object : TypeToken<List<PlaceToEat>>() {}.type
        return Gson().fromJson(reader, placeType)
    }

    private fun showLocationError() {
        // Отображаем заглушку или сообщение об ошибке
        Log.e(TAG, "Не удалось получить местоположение")
        Toast.makeText(this, "Не удалось получить местоположение", Toast.LENGTH_SHORT).show()
    }

    private fun showPermissionDeniedMessage() {
        // Сообщение, если пользователь отказался от предоставления разрешения
        Toast.makeText(this, "Для корректной работы необходимо разрешение на доступ к местоположению", Toast.LENGTH_LONG).show()
    }

    // Обрабатываем результат запроса разрешений
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Разрешение получено, вызываем получение местоположения
                    Log.d(TAG, "Разрешение на доступ к местоположению получено")
                    getLocation()
                } else {
                    // Разрешение отклонено, показываем сообщение
                    Log.d(TAG, "Разрешение на доступ к местоположению отклонено")
                    showPermissionDeniedMessage()
                }
            }
            BACKGROUND_LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // Разрешение на фоновый доступ отклонено, возможно, нужно предупредить пользователя
                    Log.d(TAG, "Разрешение на фоновый доступ отклонено")
                }
            }
        }
    }
}
