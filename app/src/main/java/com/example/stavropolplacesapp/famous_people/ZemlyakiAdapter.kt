package com.example.stavropolplacesapp.famous_people

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stavropolplacesapp.R
import com.example.stavropolplacesapp.navigation.PersonNavigation

data class FamousPeoplePerson(val name: String, val imageUrl: String, val description: String)

class ZemlyakiAdapter(private val people: List<FamousPeoplePerson>, private val onClick: (FamousPeoplePerson) -> Unit) :
    RecyclerView.Adapter<ZemlyakiAdapter.PersonViewHolder>() {

    class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val personName: TextView = itemView.findViewById(R.id.person_name)
        val personImage: ImageView = itemView.findViewById(R.id.person_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = people[position]
        holder.personName.text = person.name
        Glide.with(holder.itemView.context).load(person.imageUrl).into(holder.personImage)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            PersonNavigation.openPersonDetails(context, person)
        }
    }

    override fun getItemCount() = people.size
}
