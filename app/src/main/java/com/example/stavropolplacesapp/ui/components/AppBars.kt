package com.example.stavropolplacesapp.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stavropolplacesapp.navigation.AppRoutes
import com.example.stavropolplacesapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(route: String) {
    TopAppBar(title = { Text(topBarTitle(route)) })
}

@Composable
fun AppBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == AppRoutes.HOME,
            onClick = { onNavigate(AppRoutes.HOME) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Главная",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Главная", fontSize = 10.sp, maxLines = 1) }
        )
        NavigationBarItem(
            selected = currentRoute == AppRoutes.PLACES,
            onClick = { onNavigate(AppRoutes.PLACES) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_places),
                    contentDescription = "Места",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Места", fontSize = 10.sp, maxLines = 1) }
        )
        NavigationBarItem(
            selected = currentRoute == AppRoutes.FAVORITES,
            onClick = { onNavigate(AppRoutes.FAVORITES) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_favorite),
                    contentDescription = "Избранное",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Избранное", fontSize = 10.sp, maxLines = 1) }
        )
        NavigationBarItem(
            selected = currentRoute == AppRoutes.ABOUT,
            onClick = { onNavigate(AppRoutes.ABOUT) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.about),
                    contentDescription = "О приложении",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("О приложении", fontSize = 10.sp, maxLines = 1) }
        )
    }
}

internal fun topBarTitle(route: String): String {
    return when (route) {
        AppRoutes.PLACES -> "Места"
        AppRoutes.FAVORITES -> "Избранное"
        AppRoutes.ABOUT -> "О приложении"
        AppRoutes.AFISHA -> "Афиша"
        AppRoutes.ZEMLYAKI -> "Земляки"
        AppRoutes.EAT -> "Где поесть"
        else -> "Ставротека"
    }
}
