package com.example.stavropolplacesapp.famous_people

import android.content.Context
import com.example.stavropolplacesapp.Constants
import com.example.stavropolplacesapp.JsonUtils

data class FamousPeopleResponse(
    val people: List<FamousPeoplePerson>
)

object FamousPeopleRepository {
    fun loadPeople(context: Context): List<FamousPeoplePerson> {
        val response = JsonUtils.loadFromJson<FamousPeopleResponse>(
            context,
            Constants.PEOPLE_JSON_FILE
        )
        return response?.people ?: emptyList()
    }
}
