package com.example.stavropolplacesapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.io.IOException

class ZemlyakiActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var zemlyakiAdapter: ZemlyakiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zemlyaki)

        recyclerView = findViewById(R.id.recyclerView_zemlyaki)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val people = getPeopleFromFile() // Получаем людей из JSON-файла
        zemlyakiAdapter = ZemlyakiAdapter(people) { person ->
            val intent = Intent(this, PersonDetailActivity::class.java).apply {
                putExtra("personName", person.name)
                putExtra("imageUrl", person.imageUrl)
            }
            startActivity(intent)
        }

        recyclerView.adapter = zemlyakiAdapter
    }

    private fun getPeopleFromFile(): List<Person> {
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

    private fun parsePeopleFromJson(jsonString: String): List<Person> {
        val peopleList = mutableListOf<Person>()
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("people")

        for (i in 0 until jsonArray.length()) {
            val jsonObjectPerson = jsonArray.getJSONObject(i)
            val name = jsonObjectPerson.getString("name")
            val imageUrl = jsonObjectPerson.getString("imageUrl")
            peopleList.add(Person(name, imageUrl))
        }
        return peopleList
    }
}
