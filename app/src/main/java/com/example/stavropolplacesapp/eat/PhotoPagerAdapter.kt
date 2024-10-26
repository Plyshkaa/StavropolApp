package com.example.stavropolplacesapp.eat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stavropolplacesapp.R

class PhotoPagerAdapter(
    private val context: Context,
    private val photoUrls: Array<String>
) : RecyclerView.Adapter<PhotoPagerAdapter.PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_pager, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoUrl = photoUrls[position]
        Glide.with(holder.itemView.context).load(photoUrl).into(holder.photoImageView)

        // Добавляем обработчик клика для открытия полноэкранного просмотра
        holder.photoImageView.setOnClickListener {
            val intent = Intent(context, FullScreenImageActivity::class.java).apply {
                putExtra("photos", photoUrls)
                putExtra("position", position)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = photoUrls.size

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.photo_image_view)
    }
}
