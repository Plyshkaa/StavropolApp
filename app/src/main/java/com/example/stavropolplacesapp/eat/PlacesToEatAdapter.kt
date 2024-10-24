package com.example.stavropolplacesapp.eat

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stavropolplacesapp.R
import com.example.stavropolplacesapp.places.PlaceDetailActivity
import com.google.gson.Gson

class PlacesToEatAdapter(
    private val places: List<PlaceToEat>,
    private val currentLat: Double,
    private val currentLon: Double
) : RecyclerView.Adapter<PlacesToEatAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_place_to_eat, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PlaceEatDetailActivity::class.java)
            intent.putExtra("placeName", place.name)
            intent.putExtra("placeDescription", place.description)
            intent.putExtra("placeAddress", place.address)
            intent.putExtra("placePhone", place.phone)
            intent.putExtra("placePhotos", place.photos.toTypedArray())
            intent.putExtra("placeCoordinates", "${place.coordinates.latitude}, ${place.coordinates.longitude}")

            // Преобразуем объект WorkingDays в строку JSON
            val workingHoursJson = Gson().toJson(place.working_hours)  // изменил на working_hours
            intent.putExtra("placeWorkingHours", workingHoursJson)

            holder.itemView.context.startActivity(intent)
            // Проверяем, что placePhotos не null
            val photosArray = place.photos?.toTypedArray() ?: emptyArray()
            intent.putExtra("placePhotos", photosArray)
        }
    }


    override fun getItemCount(): Int {
        return places.size
    }

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeImageView: ImageView = itemView.findViewById(R.id.place_image)
        val placeNameTextView: TextView = itemView.findViewById(R.id.place_name_text_view)
        val placeDistanceTextView: TextView = itemView.findViewById(R.id.place_distance_text_view)

        fun bind(place: PlaceToEat) {
            placeNameTextView.text = place.name

            // Загружаем изображение
            Glide.with(itemView.context)
                .load(place.photos[0]) // Загрузим первую фотографию
                .into(placeImageView)

            // Рассчитываем расстояние
            val results = FloatArray(1)
            android.location.Location.distanceBetween(
                currentLat, currentLon,
                place.coordinates.latitude, place.coordinates.longitude,
                results
            )
            val distanceInKm = results[0] / 1000
            placeDistanceTextView.text = String.format("%.1f км", distanceInKm)
        }
    }
}
