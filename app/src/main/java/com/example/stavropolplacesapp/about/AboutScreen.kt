package com.example.stavropolplacesapp.about

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.example.stavropolplacesapp.R


import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat


class AboutScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)  // Убедись, что у тебя есть файл activity_about.xml

        // Инициализация Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_about)
        setSupportActionBar(toolbar)
        // Добавляем кнопку назад
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setTint(
            ContextCompat.getColor(
                this,
                R.color.black
            )
        ) // Устанавливаем чёрный цвет
        val titleTextView: TextView = findViewById(R.id.toolbar_title_about)
        titleTextView.text = "О приложении"
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

