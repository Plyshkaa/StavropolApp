package com.example.stavropolplacesapp

import android.content.Context
import android.util.Log
import com.example.stavropolplacesapp.Constants
import com.example.stavropolplacesapp.places.Place
import com.example.stavropolplacesapp.places.PlacesResponseList
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.IOException

object JsonUtils {
    
    const val TAG = Constants.TAG_JSON_UTILS
    private const val PLACES_FILE = Constants.PLACES_JSON_FILE
    
    // Кэшируем Gson для лучшей производительности
    val gson: Gson by lazy {
        GsonBuilder()
            .setLenient()
            .create()
    }

    fun loadPlacesFromJson(context: Context): List<Place>? {
        return try {
            context.assets.open(PLACES_FILE).use { inputStream ->
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                Log.d(TAG, "JSON загружен, размер: ${jsonString.length} символов")

                val type = object : TypeToken<PlacesResponseList>() {}.type
                val placesResponse = gson.fromJson<PlacesResponseList>(jsonString, type)
                Log.d(TAG, "Места загружены: ${placesResponse.places.size}")
                
                placesResponse.places
            }
        } catch (e: IOException) {
            Log.e(TAG, "Ошибка при чтении файла $PLACES_FILE: ${e.message}")
            null
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при парсинге JSON: ${e.message}")
            null
        }
    }

    // Добавляем функцию для загрузки других JSON файлов
    inline fun <reified T> loadFromJson(context: Context, fileName: String): T? {
        return try {
            context.assets.open(fileName).use { inputStream ->
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                val type = object : TypeToken<T>() {}.type
                gson.fromJson<T>(jsonString, type)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Ошибка при чтении файла $fileName: ${e.message}")
            null
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при парсинге JSON из $fileName: ${e.message}")
            null
        }
    }
}

