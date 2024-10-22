package com.example.stavropolplacesapp.about

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.example.stavropolplacesapp.R

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.stavropolplacesapp.MainActivity
import com.example.stavropolplacesapp.places.PlacesActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AboutScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

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
                    // Мы уже на экране "О приложении", нет необходимости переходить
                    true
                }
                else -> false
            }
        }

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_about)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "О приложении" // Устанавливаем заголовок
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true) // Должно быть true для отображения заголовка


        // Устанавливаем заголовок для тулбара
        supportActionBar?.title = "О приложении"

        // Устанавливаем иконку назад и цвет
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setTint(
            ContextCompat.getColor(this, R.color.black)
        )

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
    }
}
