import android.content.Context
import android.util.Log
import com.google.gson.Gson
import java.io.IOException

object JsonUtils {
    fun loadPlacesFromJson(context: Context): List<Place>? {
        return try {
            // Открываем файл places.json из assets
            val inputStream = context.assets.open("places.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            Log.d("JsonUtils", "JSON String: $jsonString") // Логируем JSON строку

            // Используем Gson для десериализации
            val gson = Gson()
            val placesResponse = gson.fromJson(jsonString, PlacesResponse::class.java)

            Log.d(
                "JsonUtils",
                "Places loaded: ${placesResponse.places.size}"
            ) // Логируем количество мест
            placesResponse.places // Возвращаем список мест
        } catch (e: IOException) {
            Log.e("JsonUtils", "Ошибка при загрузке мест: ${e.message}")
            null // Возвращаем null в случае ошибки
        } catch (e: Exception) {
            Log.e("JsonUtils", "Ошибка: ${e.message}")
            null // Возвращаем null в случае ошибки
        }
    }
}

