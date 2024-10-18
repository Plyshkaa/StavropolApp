package com.example.stavropolplacesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class RegionDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var regionAdapter: RegionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_region_detail)

        recyclerView = findViewById(R.id.recyclerView_region)
        recyclerView.layoutManager = LinearLayoutManager(this)

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
