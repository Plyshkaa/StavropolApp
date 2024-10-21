package com.example.stavropolplacesapp.famous_people

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stavropolplacesapp.R
import org.json.JSONObject
import java.io.IOException
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat


class ZemlyakiActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var zemlyakiAdapter: ZemlyakiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zemlyaki)

        // Инициализация Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Добавляем кнопку назад
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setTint(
            ContextCompat.getColor(
                this,
                R.color.black
            )
        ) // Устанавливаем чёрный цвет
        val titleTextView: TextView = findViewById(R.id.toolbar_title)
        titleTextView.text = "Земляки"
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



        recyclerView = findViewById(R.id.recyclerView_zemlyaki)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val people = getPeopleFromFile() // Получаем людей из JSON-файла
        zemlyakiAdapter = ZemlyakiAdapter(people) { person ->
            val intent = Intent(this, PersonDetailActivity::class.java).apply {
                putExtra("personName", person.name)
                putExtra("imageUrl", person.imageUrl)
                putExtra("description", person.description)
            }
            startActivity(intent)
        }

        recyclerView.adapter = zemlyakiAdapter
    }

    private fun getPeopleFromFile(): List<FamousPeoplePerson> {
        val jsonString = loadJSONFromAsset("people.json") // Загрузка файла из assets
        return parsePeopleFromJson(jsonString!!)
    }

    private fun loadJSONFromAsset(filename: String): String? {
        return try {
            val inputStream = assets.open(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

    // Обрабатываем нажатие кнопки "Назад"
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun parsePeopleFromJson(jsonString: String): List<FamousPeoplePerson> {
        val peopleList = mutableListOf<FamousPeoplePerson>()

        // Преобразуем строку JSON в объект
        val jsonObject = JSONObject(jsonString)

        // Получаем массив "people"
        val jsonArray = jsonObject.getJSONArray("people")

        // Проходим по массиву
        for (i in 0 until jsonArray.length()) {
            val jsonObjectPerson = jsonArray.getJSONObject(i)
            val name = jsonObjectPerson.getString("name")
            val imageUrl = jsonObjectPerson.getString("imageUrl")
            val description = jsonObjectPerson.getString("description") // Извлекаем описание
            peopleList.add(
                FamousPeoplePerson(
                    name,
                    imageUrl,
                    description
                )
            ) // Добавляем описание в Person
        }
        return peopleList
    }


}
