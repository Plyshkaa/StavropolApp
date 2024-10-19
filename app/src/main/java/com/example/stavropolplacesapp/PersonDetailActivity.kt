package com.example.stavropolplacesapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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
        val personDescription = intent.getStringExtra("description") ?: "" // Подстраховка от null

        // Привязываем элементы к переменным
        val nameTextView: TextView = findViewById(R.id.person_detail_name)
        val personImageView: ImageView = findViewById(R.id.person_detail_image)
        val descriptionTextView: TextView = findViewById(R.id.person_detail_description)
        val toggleButton: Button = findViewById(R.id.button_toggle_description)

        // Устанавливаем данные
        nameTextView.text = personName
        Glide.with(this).load(imageUrl).into(personImageView)

        // Отображаем краткое описание
        updateDescription(personDescription, descriptionTextView, toggleButton)

        // Обработчик для кнопки "Показать больше/меньше"
        toggleButton.setOnClickListener {
            isExpanded = !isExpanded
            updateDescription(personDescription, descriptionTextView, toggleButton)
        }
    }

    // Функция для обновления отображаемого текста
    private fun updateDescription(description: String, textView: TextView, button: Button) {
        if (isExpanded) {
            textView.text = description
            button.text = "Скрыть" // Меняем текст кнопки
        } else {
            // Если текст больше максимальной длины, обрезаем его и добавляем "..."
            if (description.length > maxLength) {
                val shortDescription = description.substring(0, maxLength) + "..."
                textView.text = shortDescription
                button.text = "Показать больше" // Меняем текст кнопки на "Показать больше"
            } else {
                textView.text = description
                button.text = "" // Если текст короткий, скрываем кнопку
            }
        }
    }
}
