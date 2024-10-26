package com.example.stavropolplacesapp.region

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stavropolplacesapp.MainActivity
import com.example.stavropolplacesapp.R
import com.example.stavropolplacesapp.about.AboutScreen
import com.example.stavropolplacesapp.places.PlacesActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject

class RegionDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var regionAdapter: RegionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_region_detail)

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
                    // Открываем экран "О приложении"
                    val intent = Intent(this, AboutScreen::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        recyclerView = findViewById(R.id.recyclerView_region)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Настройка тулбара
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Убираем стандартное название приложения в Toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.black)) // Устанавливаем чёрный цвет
        // Настраиваем кастомный заголовок
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        toolbarTitle.text = "Ставропольский край"  // Устанавливаем текст "Места" в тулбаре

        // Обработчик для кнопки назад
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Прозрачный статус-бар с видимыми иконками
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        // Используем светлый статус-бар для видимых иконок (черные иконки)
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        // Загрузка данных из JSON-файла
        val regionDetails = loadRegionDetailsFromJson("region.json")
        regionAdapter = RegionAdapter(regionDetails)
        recyclerView.adapter = regionAdapter
    }

    // Метод для загрузки данных из JSON-файла
    private fun loadRegionDetailsFromJson(filename: String): List<RegionDetail> {
        val jsonString = assets.open(filename).bufferedReader().use { it.readText() }
        val regionList = mutableListOf<RegionDetail>()

        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("region_details")

        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            val title = item.getString("title")
            val description = item.getString("description")
            val imageUrl = item.getString("imageUrl")
            regionList.add(RegionDetail(title, description, imageUrl))
        }

        return regionList
    }
}

// Data class для региона
data class RegionDetail(val title: String, val description: String, val imageUrl: String)
