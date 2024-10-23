package com.example.stavropolplacesapp.eat

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.stavropolplacesapp.MainActivity
import com.example.stavropolplacesapp.R
import com.example.stavropolplacesapp.about.AboutScreen
import com.example.stavropolplacesapp.places.PlacesActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class PlaceDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eat_place_detail)

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

        // Получаем данные из Intent
        val placeName = intent.getStringExtra("placeName")
        val placeDescription = intent.getStringExtra("placeDescription")
        val placeAddress = intent.getStringExtra("placeAddress")
        val placePhotos = intent.getStringArrayExtra("placePhotos")


        // Инициализация Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_places_to_eat_detail)
        setSupportActionBar(toolbar)
        // Добавляем кнопку назад
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = placeName
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setTint(
            ContextCompat.getColor(
                this,
                R.color.black
            )
        ) // Устанавливаем чёрный цвет

        // Добавляем действие при нажатии на кнопку возврата
        toolbar.setNavigationOnClickListener {
            onBackPressed() // Возврат на предыдущий экран
        }
        // Прозрачный статус-бар с видимыми иконками
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        // Используем светлый статус-бар для видимых иконок (черные иконки)
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Инициализация виджетов
        val placeNameTextView: TextView = findViewById(R.id.place_name)
        val placeDescriptionTextView: TextView = findViewById(R.id.place_description)
        val placeAddressTextView: TextView = findViewById(R.id.place_address)

        // Установка данных
        placeNameTextView.text = placeName
        placeDescriptionTextView.text = placeDescription
        placeAddressTextView.text = placeAddress

        // Настройка ViewPager для фото
        val viewPager: ViewPager2 = findViewById(R.id.photo_view_pager)
        if (placePhotos != null && placePhotos.isNotEmpty()) {
            val adapter = PhotoPagerAdapter(placePhotos)
            viewPager.adapter = adapter
        }
    }
}
