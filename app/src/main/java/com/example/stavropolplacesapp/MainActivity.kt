package com.example.stavropolplacesapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stavropolplacesapp.presentation.places.PlacesScreen
import com.example.stavropolplacesapp.navigation.PlaceExtras
import com.example.stavropolplacesapp.presentation.favorites.FavoritesScreen
import com.example.stavropolplacesapp.presentation.about.AboutScreenCompose
import com.example.stavropolplacesapp.presentation.afisha.AfishaScreen
import com.example.stavropolplacesapp.presentation.famous_people.ZemlyakiScreen
import com.example.stavropolplacesapp.presentation.eat.PlacesToEatScreen
import com.example.stavropolplacesapp.presentation.eat.PlaceEatDetailScreen
import com.example.stavropolplacesapp.presentation.eat.PlaceEatDetailUi
import com.example.stavropolplacesapp.navigation.EatExtras
import com.example.stavropolplacesapp.eat.PlaceToEat
import com.example.stavropolplacesapp.presentation.places.PlaceDetailScreen
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import com.example.stavropolplacesapp.region.RegionDetailActivity
import com.example.stavropolplacesapp.navigation.AppRoutes
import com.example.stavropolplacesapp.navigation.NavExtras
import com.example.stavropolplacesapp.ui.components.AppBottomBar
import com.example.stavropolplacesapp.ui.components.AppTopBar
import com.example.stavropolplacesapp.utils.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    companion object {
        private const val TAG = Constants.TAG_MAIN_ACTIVITY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startRoute = when (intent.getStringExtra(NavExtras.START_ROUTE)) {
            AppRoutes.HOME,
            AppRoutes.PLACES,
            AppRoutes.FAVORITES,
            AppRoutes.ABOUT,
            AppRoutes.AFISHA,
            AppRoutes.ZEMLYAKI,
            AppRoutes.EAT,
            AppRoutes.EAT_DETAIL,
            AppRoutes.PLACE_DETAIL ->
                intent.getStringExtra(NavExtras.START_ROUTE)
            else -> AppRoutes.HOME
        } ?: AppRoutes.HOME
        setContent {
            MaterialTheme {
                MainRoot(startRoute)
            }
        }
        checkLocationServiceAndPermission()
    }

    private fun checkLocationServiceAndPermission() {
        if (!PermissionUtils.isLocationEnabled(this)) {
            PermissionUtils.showLocationDialog(
                this,
                onPositiveClick = { PermissionUtils.openLocationSettings(this) },
                onNegativeClick = { Log.w(TAG, "Геолокация не включена пользователем") }
            )
        } else {
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        if (!PermissionUtils.hasLocationPermission(this)) {
            Log.d(TAG, "Запрос разрешения на доступ к местоположению")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Constants.LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            onPermissionGranted()
        }
    }

    private fun onPermissionGranted() {
        Log.d(TAG, "Разрешение на доступ к местоположению получено")
        // Логика для работы с местоположением
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted()
            } else {
                Log.d(TAG, "Пользователь отклонил запрос на доступ к местоположению")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainRoot(startRoute: String) {
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = backStackEntry?.destination?.route ?: startRoute
    Scaffold(
        topBar = {
            when (currentRoute) {
                AppRoutes.PLACE_DETAIL -> {
                    val place = navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<com.example.stavropolplacesapp.places.Place>(PlaceExtras.PLACE)
                    TopAppBar(
                        title = { Text(place?.name ?: "Место") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_back),
                                    contentDescription = "Назад"
                                )
                            }
                        }
                    )
                }
                AppRoutes.EAT_DETAIL -> {
                    val place = navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<PlaceToEat>(EatExtras.PLACE)
                    TopAppBar(
                        title = { Text(place?.name ?: "Где поесть") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_back),
                                    contentDescription = "Назад"
                                )
                            }
                        }
                    )
                }
                else -> AppTopBar(currentRoute)
            }
        },
        bottomBar = {
            AppBottomBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    if (currentRoute != route) {
                        navController.navigate(route)
                    }
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable(AppRoutes.HOME) {
                HomeScreen(
                    onPlacesClick = { navController.navigate(AppRoutes.PLACES) },
                    onRegionClick = { navController.context.startActivity(Intent(navController.context, RegionDetailActivity::class.java)) },
                    onFamousClick = { navController.navigate(AppRoutes.ZEMLYAKI) },
                    onEatClick = { navController.navigate(AppRoutes.EAT) },
                    onAfishaClick = { navController.navigate(AppRoutes.AFISHA) }
                )
            }
            composable(AppRoutes.PLACES) {
                val context = navController.context
                PlacesScreen(
                    onPlaceClick = { place ->
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(PlaceExtras.PLACE, place)
                        navController.navigate(AppRoutes.PLACE_DETAIL)
                    }
                )
            }
            composable(AppRoutes.FAVORITES) {
                val context = navController.context
                FavoritesScreen(
                    onPlaceClick = { place ->
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(PlaceExtras.PLACE, place)
                        navController.navigate(AppRoutes.PLACE_DETAIL)
                    }
                )
            }
            composable(AppRoutes.ABOUT) {
                AboutScreenCompose()
            }
            composable(AppRoutes.AFISHA) {
                AfishaScreen()
            }
            composable(AppRoutes.ZEMLYAKI) {
                ZemlyakiScreen()
            }
            composable(AppRoutes.EAT) {
                PlacesToEatScreen(
                    onOpenDetails = { place ->
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(EatExtras.PLACE, place)
                        navController.navigate(AppRoutes.EAT_DETAIL)
                    }
                )
            }
            composable(AppRoutes.EAT_DETAIL) {
                val place = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<PlaceToEat>(EatExtras.PLACE)
                val data = if (place != null) {
                    PlaceEatDetailUi(
                        name = place.name,
                        description = place.description,
                        address = place.address,
                        phone = place.phone,
                        coordinates = "${place.coordinates.latitude}, ${place.coordinates.longitude}",
                        photos = place.photos,
                        workingHours = place.working_hours
                    )
                } else {
                    PlaceEatDetailUi(
                        name = "",
                        description = "",
                        address = "",
                        phone = "",
                        coordinates = "",
                        photos = emptyList(),
                        workingHours = null
                    )
                }
                PlaceEatDetailScreen(data)
            }
            composable(AppRoutes.PLACE_DETAIL) {
                val place = navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.get<com.example.stavropolplacesapp.places.Place>(PlaceExtras.PLACE)
                    ?: navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<com.example.stavropolplacesapp.places.Place>(PlaceExtras.PLACE)
                val context = navController.context
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                var userLocation by remember { mutableStateOf<android.location.Location?>(null) }
                LaunchedEffect(Unit) {
                    userLocation = try {
                        suspendCancellableCoroutine { cont ->
                            fusedLocationClient.lastLocation
                                .addOnSuccessListener { cont.resume(it) }
                                .addOnFailureListener { cont.resume(null) }
                        }
                    } catch (_: Exception) {
                        null
                    }
                }
                if (place == null) {
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                } else {
                    PlaceDetailScreen(place, userLocation)
                }
            }
        }
    }
}


@Composable
private fun HomeScreen(
    onPlacesClick: () -> Unit,
    onRegionClick: () -> Unit,
    onFamousClick: () -> Unit,
    onEatClick: () -> Unit,
    onAfishaClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        HomeCard(
            title = "Места которые стоит посетить",
            imageRes = R.drawable.ic_places_static_image,
            height = 160.dp,
            onClick = onPlacesClick
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            HomeCard(
                title = "О регионе",
                imageRes = R.drawable.ic_region,
                height = 160.dp,
                modifier = Modifier.weight(1f),
                onClick = onRegionClick
            )
            Spacer(modifier = Modifier.width(12.dp))
            HomeCard(
                title = "Земляки",
                imageRes = R.drawable.ic_zemlyki,
                height = 160.dp,
                modifier = Modifier.weight(1f),
                onClick = onFamousClick
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        HomeCard(
            title = "Где поесть",
            imageRes = R.drawable.ic_where_to_eat,
            height = 100.dp,
            onClick = onEatClick
        )
        Spacer(modifier = Modifier.height(16.dp))
        HomeCard(
            title = "Афиша",
            imageRes = R.drawable.ic_afisha_placeholder,
            height = 160.dp,
            onClick = onAfishaClick
        )
    }
}

@Composable
private fun HomeCard(
    title: String,
    imageRes: Int,
    height: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x33000000))
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
