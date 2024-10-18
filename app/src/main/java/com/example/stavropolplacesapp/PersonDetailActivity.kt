package com.example.stavropolplacesapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class PersonDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_detail)

        // Получаем данные из интента
        val personName = intent.getStringExtra("personName")
        val imageUrl = intent.getStringExtra("imageUrl")
        val personDescription = intent.getStringExtra("description")

        // Привязываем элементы к переменным
        val nameTextView: TextView = findViewById(R.id.person_detail_name)
        val personImageView: ImageView = findViewById(R.id.person_detail_image)
        val descriptionTextView: TextView = findViewById(R.id.person_detail_description)

        // Устанавливаем данные в UI
        nameTextView.text = personName
        descriptionTextView.text = personDescription // Отображаем описание
        Glide.with(this).load(imageUrl).into(personImageView)
    }
}
