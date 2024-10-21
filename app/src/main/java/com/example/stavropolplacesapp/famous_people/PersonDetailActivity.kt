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
import androidx.core.content.ContextCompat
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.black)) // Устанавливаем чёрный цвет
        // Устанавливаем заголовок в Toolbar
        supportActionBar?.title = personName

        // Прозрачный статус-бар с черными иконками
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT


        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }




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
