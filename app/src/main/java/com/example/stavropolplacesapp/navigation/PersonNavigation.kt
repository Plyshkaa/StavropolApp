package com.example.stavropolplacesapp.navigation

import android.content.Context
import android.content.Intent
import com.example.stavropolplacesapp.famous_people.FamousPeoplePerson
import com.example.stavropolplacesapp.famous_people.PersonDetailActivity

object PersonNavigation {
    fun openPersonDetails(context: Context, person: FamousPeoplePerson) {
        val intent = Intent(context, PersonDetailActivity::class.java).apply {
            putExtra("personName", person.name)
            putExtra("imageUrl", person.imageUrl)
            putExtra("description", person.description)
        }
        context.startActivity(intent)
    }
}
