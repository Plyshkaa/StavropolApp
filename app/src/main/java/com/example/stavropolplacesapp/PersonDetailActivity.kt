package com.example.stavropolplacesapp

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class PersonDetailActivity : AppCompatActivity() {

    private var isExpanded = false
    private val maxLength = 150 // Количество символов для краткого описания

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_detail)

        // Получаем данные из интента
        val personName = intent.getStringExtra("personName")
        val imageUrl = intent.getStringExtra("imageUrl")
        val personDescriptionRaw = intent.getStringExtra("description") ?: "" // Описание как строка

        // Привязываем элементы к переменным
        val nameTextView: TextView = findViewById(R.id.person_detail_name)
        val personImageView: ImageView = findViewById(R.id.person_detail_image)
        val descriptionTextView: TextView = findViewById(R.id.person_detail_description)
        val toggleButton: Button = findViewById(R.id.button_toggle_description)

        // Устанавливаем данные
        nameTextView.text = personName
        Glide.with(this).load(imageUrl).into(personImageView)

        // Конвертируем текст с HTML форматированием
        val personDescription = Html.fromHtml(personDescriptionRaw, Html.FROM_HTML_MODE_LEGACY)

        // Отображаем текст (сначала сокращённый)
        descriptionTextView.text = if (personDescription.length > maxLength) {
            personDescription.subSequence(0, maxLength).toString() + "..."
        } else {
            personDescription
        }

        // Обработчик для кнопки "Показать больше/меньше"
        toggleButton.setOnClickListener {
            isExpanded = !isExpanded
            if (isExpanded) {
                descriptionTextView.text = personDescription
                toggleButton.text = "Скрыть"
            } else {
                descriptionTextView.text = if (personDescription.length > maxLength) {
                    personDescription.subSequence(0, maxLength).toString() + "..."
                } else {
                    personDescription
                }
                toggleButton.text = "Показать больше"
            }
        }
    }
}
