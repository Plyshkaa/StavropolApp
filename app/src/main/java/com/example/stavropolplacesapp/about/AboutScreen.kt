package com.example.stavropolplacesapp.about

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.example.stavropolplacesapp.R

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
        bottomNavigationView.selectedItemId = R.id.nav_about

        // Находим TextView для email
        val emailTextView: TextView = findViewById(R.id.contact_info)
        // Обработка нажатия на email
        emailTextView.setOnClickListener {
            openEmailClient(emailTextView.text.toString())
        }

        // Устанавливаем обработчик для навигации
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Открываем экран "Главная"
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_places -> {
                    // Открываем экран "Места"
                    val intent = Intent(this, PlacesActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_favorites -> {
                    // Открываем экран "Избранное"
                    val intent = Intent(this, com.example.stavropolplacesapp.favorites.FavoritesActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_about -> {
                    // Мы уже на экране "О приложении", нет необходимости переходить
                    true
                }
                else -> false
            }
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar_about)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
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
    private fun openEmailClient(email: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        }

        try {
            startActivity(Intent.createChooser(intent, "Выберите почтовое приложение"))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this, "Нет установленных почтовых приложений", Toast.LENGTH_SHORT).show()
        }
    }
    // Открыть YouTube
    fun openYouTube(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://rutube.ru/u/svoe/"))
        startActivity(intent)
    }

    // Открыть ВКонтакте
    fun openVK(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/stav_region"))
        startActivity(intent)
    }

    // Открыть Telegram
    fun openTelegram(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/s/stavropolye_tv"))
        startActivity(intent)
    }
}
