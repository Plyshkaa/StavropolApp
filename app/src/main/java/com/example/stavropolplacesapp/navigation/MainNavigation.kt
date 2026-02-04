package com.example.stavropolplacesapp.navigation

import android.content.Context
import android.content.Intent
import com.example.stavropolplacesapp.MainActivity

object MainNavigation {
    fun openRoute(context: Context, route: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(NavExtras.START_ROUTE, route)
        }
        context.startActivity(intent)
    }
}
