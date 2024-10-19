package com.example.stavropolplacesapp.region

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stavropolplacesapp.R

class RegionAdapter(private val regionDetails: List<RegionDetail>) : RecyclerView.Adapter<RegionAdapter.RegionViewHolder>() {

    class RegionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val regionTitle: TextView = itemView.findViewById(R.id.region_title)
        val regionImage: ImageView = itemView.findViewById(R.id.region_image)
        val regionDescription: TextView = itemView.findViewById(R.id.region_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_region, parent, false)
        return RegionViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        val region = regionDetails[position]
        holder.regionTitle.text = region.title
        holder.regionDescription.text = region.description
        Glide.with(holder.itemView.context).load(region.imageUrl).into(holder.regionImage)
    }

    override fun getItemCount() = regionDetails.size
}
