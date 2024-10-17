import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stavropolplacesapp.PlaceDetailActivity
import com.squareup.picasso.Picasso // Если используешь Picasso для загрузки изображений
import com.example.stavropolplacesapp.R

class PlacesAdapter(private var places: List<Place>) : RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        // Создаем новый элемент для списка
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.nameTextView.text = place.name
        holder.descriptionTextView.text = place.description
        Picasso.get().load(place.imageUrl).into(holder.imageView)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PlaceDetailActivity::class.java).apply {
                putExtra("name", place.name)
                putExtra("description", place.description)
                putExtra("imageUrl", place.imageUrl)
                putExtra("latitude", place.latitude)
                putExtra("longitude", place.longitude)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        // Возвращаем общее количество элементов в списке
        return places.size
    }

    fun updateData(newPlaces: List<Place>) {
        // Обновляем данные и уведомляем адаптер об изменениях
        places = newPlaces
        notifyDataSetChanged()
    }

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Инициализируем элементы представления
        val nameTextView: TextView = itemView.findViewById(R.id.placeName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.placeDescription)
        val imageView: ImageView = itemView.findViewById(R.id.placeImage) // Убедитесь, что этот ID соответствует вашему XML
    }
}
