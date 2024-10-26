package com.example.stavropolplacesapp.eat

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.stavropolplacesapp.R

class FullScheduleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_schedule)



        // Получаем переданные данные о расписании (Map из Intent)
        val placeWorkingHours = intent.getSerializableExtra("placeWorkingHours") as? WorkingDays

        // Инициализация виджетов для каждого дня недели
        val mondayTextView: TextView = findViewById(R.id.monday_hours)
        val tuesdayTextView: TextView = findViewById(R.id.tuesday_hours)
        val wednesdayTextView: TextView = findViewById(R.id.wednesday_hours)
        val thursdayTextView: TextView = findViewById(R.id.thursday_hours)
        val fridayTextView: TextView = findViewById(R.id.friday_hours)
        val saturdayTextView: TextView = findViewById(R.id.saturday_hours)
        val sundayTextView: TextView = findViewById(R.id.sunday_hours)

        // Установка данных для каждого дня недели
        mondayTextView.text = "Понедельник: ${placeWorkingHours?.monday}"
        tuesdayTextView.text = "Вторник: ${placeWorkingHours?.tuesday}"
        wednesdayTextView.text = "Среда: ${placeWorkingHours?.wednesday}"
        thursdayTextView.text = "Четверг: ${placeWorkingHours?.thursday}"
        fridayTextView.text = "Пятница: ${placeWorkingHours?.friday}"
        saturdayTextView.text = "Суббота: ${placeWorkingHours?.saturday}"
        sundayTextView.text = "Воскресенье: ${placeWorkingHours?.sunday}"
    }
}
