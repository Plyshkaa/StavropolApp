package com.example.stavropolplacesapp.eat

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.stavropolplacesapp.MainActivity
import com.example.stavropolplacesapp.R
import com.example.stavropolplacesapp.about.AboutScreen
import com.example.stavropolplacesapp.places.PlacesActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import java.util.Calendar

class PlaceEatDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eat_place_detail)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        // Обработчик для навигации
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_places -> {
                    val intent = Intent(this, PlacesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_about -> {
                    val intent = Intent(this, AboutScreen::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Получаем данные из Intent
        val placeName = intent.getStringExtra("placeName") ?: "Название не указано"
        val placeDescription = intent.getStringExtra("placeDescription")
        val placeAddress = intent.getStringExtra("placeAddress")
        val placePhone = intent.getStringExtra("placePhone")
        val placePhotos = intent.getStringArrayExtra("placePhotos")
        val placeCoordinates = intent.getStringExtra("placeCoordinates")
        val placeWorkingHoursJson = intent.getStringExtra("placeWorkingHours")
        val placeWorkingHours = Gson().fromJson(placeWorkingHoursJson, WorkingDays::class.java)

        // Настраиваем Toolbar и устанавливаем название заведения как заголовок
        val toolbar: Toolbar = findViewById(R.id.toolbar_places_to_eat_detail)
        // Устанавливаем заголовок в кастомный TextView
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        toolbarTitle.text = placeName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.black))

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Прозрачный статус-бар
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Остальная инициализация виджетов
        val placeDescriptionTextView: TextView = findViewById(R.id.place_description)
        val placeAddressTextView: TextView = findViewById(R.id.place_address)
        val placeCoordinatesTextView: TextView = findViewById(R.id.place_coordinates)
        val placePhoneTextView: TextView = findViewById(R.id.place_phone)
        val placeHoursTextView: TextView = findViewById(R.id.place_hours)
        val expandIcon: ImageView = findViewById(R.id.expand_icon)

        placeDescriptionTextView.text = placeDescription
        placeAddressTextView.text = placeAddress
        placeCoordinatesTextView.text = placeCoordinates
        placePhoneTextView.text = placePhone

        // Устанавливаем действие для открытия полного расписания
        val fullScheduleClickListener = View.OnClickListener {
            if (placeWorkingHours != null) {
                showFullSchedule(placeWorkingHours)
            } else {
                Log.d("PlaceEatDetailActivity", "Place working hours: $placeWorkingHours")
            }
        }

        placeHoursTextView.setOnClickListener(fullScheduleClickListener)
        expandIcon.setOnClickListener(fullScheduleClickListener)

        // Получаем текущий день недели для отображения рабочего времени
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val workingHours = when (dayOfWeek) {
            Calendar.MONDAY -> "Сегодня: ${placeWorkingHours?.monday}"
            Calendar.TUESDAY -> "Сегодня: ${placeWorkingHours?.tuesday}"
            Calendar.WEDNESDAY -> "Сегодня: ${placeWorkingHours?.wednesday}"
            Calendar.THURSDAY -> "Сегодня: ${placeWorkingHours?.thursday}"
            Calendar.FRIDAY -> "Сегодня: ${placeWorkingHours?.friday}"
            Calendar.SATURDAY -> "Сегодня: ${placeWorkingHours?.saturday}"
            Calendar.SUNDAY -> "Сегодня: ${placeWorkingHours?.sunday}"
            else -> "Часы работы не указаны"
        }
        placeHoursTextView.text = workingHours

        // Настройка ViewPager для фото
        val viewPager: ViewPager2 = findViewById(R.id.photo_view_pager)
        if (placePhotos != null && placePhotos.isNotEmpty()) {
            val adapter = PhotoPagerAdapter(placePhotos)
            viewPager.adapter = adapter
        }

        // Клик по координатам для открытия в карте
        placeCoordinatesTextView.setOnClickListener {
            val geoUri = "geo:${placeCoordinates}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
            startActivity(intent)
        }
    }

    // Функция для отображения BottomSheetDialog с полным расписанием
    private fun showFullSchedule(placeWorkingHours: WorkingDays) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_bottom_sheet_schedule, null)
        bottomSheetDialog.setContentView(view)

        val mondayTextView: TextView = view.findViewById(R.id.monday_hours)
        val tuesdayTextView: TextView = view.findViewById(R.id.tuesday_hours)
        val wednesdayTextView: TextView = view.findViewById(R.id.wednesday_hours)
        val thursdayTextView: TextView = view.findViewById(R.id.thursday_hours)
        val fridayTextView: TextView = view.findViewById(R.id.friday_hours)
        val saturdayTextView: TextView = view.findViewById(R.id.saturday_hours)
        val sundayTextView: TextView = view.findViewById(R.id.sunday_hours)

        mondayTextView.text = placeWorkingHours.monday
        tuesdayTextView.text = placeWorkingHours.tuesday
        wednesdayTextView.text = placeWorkingHours.wednesday
        thursdayTextView.text = placeWorkingHours.thursday
        fridayTextView.text = placeWorkingHours.friday
        saturdayTextView.text = placeWorkingHours.saturday
        sundayTextView.text = placeWorkingHours.sunday

        bottomSheetDialog.show()
    }
}
