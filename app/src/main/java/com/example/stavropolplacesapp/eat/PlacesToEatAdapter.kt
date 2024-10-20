package com.example.stavropolplacesapp.eat


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stavropolplacesapp.R


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

        // Устанавливаем изображение
        Glide.with(holder.itemView.context)
            .load(place.photos[0]) // Загрузим первую фотографию
            .into(holder.placeImageView)

        // Название заведения
        holder.placeNameTextView.text = place.name

        // Рассчитываем расстояние
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            currentLat, currentLon,
            place.coordinates.latitude, place.coordinates.longitude,
            results
        )
        val distanceInKm = results[0] / 1000
        holder.placeDistanceTextView.text = String.format("%.1f км", distanceInKm)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeImageView: ImageView = itemView.findViewById(R.id.place_image)
        val placeNameTextView: TextView = itemView.findViewById(R.id.place_name_text_view)
        val placeDistanceTextView: TextView = itemView.findViewById(R.id.place_distance_text_view)
    }
}
