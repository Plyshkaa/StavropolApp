package com.example.stavropolplacesapp.places

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stavropolplacesapp.Constants
import com.example.stavropolplacesapp.databinding.ItemPlaceBinding
import com.example.stavropolplacesapp.utils.ImageUtils
import com.example.stavropolplacesapp.favorites.FavoritePlacesStore

class PlacesAdapter(private val showDescription: Boolean = true) : ListAdapter<Place, PlacesAdapter.PlaceViewHolder>(PlaceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    var onFavoriteChanged: ((placeId: Int, nowFavorite: Boolean) -> Unit)? = null

    inner class PlaceViewHolder(
        private val binding: ItemPlaceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(place: Place) {
            with(binding) {
                placeName.text = place.name
                if (showDescription) {
                    placeDescription.visibility = android.view.View.VISIBLE
                    placeDescription.text = place.description
                } else {
                    placeDescription.visibility = android.view.View.GONE
                }
                
                // Загрузка изображения с помощью ImageUtils
                ImageUtils.loadImage(itemView.context, place.imageUrl, placeImage)

                // Иконка избранного
                favoriteToggle.isSelected = place.isFavorite
                favoriteToggle.setOnClickListener {
                    val nowFav = FavoritePlacesStore.toggleFavorite(itemView.context, place.id)
                    favoriteToggle.isSelected = nowFav
                    onFavoriteChanged?.invoke(place.id, nowFav)
                }

                itemView.setOnClickListener {
                    val context = itemView.context
                    val intent = Intent(context, PlaceDetailActivity::class.java).apply {
                        putExtra(Constants.EXTRA_NAME, place.name)
                        putExtra(Constants.EXTRA_DESCRIPTION, place.description)
                        putExtra(Constants.EXTRA_FULL_DESCRIPTION, place.fullDescription)
                        putExtra(Constants.EXTRA_IMAGE_URL, place.imageUrl)
                        putExtra(Constants.EXTRA_LATITUDE, place.latitude)
                        putExtra(Constants.EXTRA_LONGITUDE, place.longitude)
                        putExtra(Constants.EXTRA_COORDINATES, "${place.latitude}°N ${place.longitude}°E")
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    private class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }
    }
}
