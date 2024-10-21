package com.example.stavropolplacesapp.famous_people

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.stavropolplacesapp.R

class PersonDetailActivity : AppCompatActivity() {

    private var isExpanded = false


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_detail)

        // Получаем данные из интента
        val personName = intent.getStringExtra("personName")
        val imageUrl = intent.getStringExtra("imageUrl")
        val personDescriptionRaw = intent.getStringExtra("description") ?: "" // Описание как строка

        // Настройка Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Настройка кнопки назад
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Детали"

        // Устанавливаем заголовок в Toolbar
        supportActionBar?.title = personName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Прозрачный статус-бар с видимыми иконками
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        // Используем светлый статус-бар для видимых иконок (черные иконки)
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR



        // Привязываем элементы к переменным

        val personImageView: ImageView = findViewById(R.id.person_detail_image)
        val descriptionTextView: TextView = findViewById(R.id.person_detail_description)


        // Устанавливаем данные

        Glide.with(this).load(imageUrl).into(personImageView)

        // Конвертируем текст с HTML форматированием
        val personDescription = Html.fromHtml(personDescriptionRaw, Html.FROM_HTML_MODE_LEGACY)

        // Отображаем текст (сначала сокращённый)
        descriptionTextView.text = personDescription

    }
}
