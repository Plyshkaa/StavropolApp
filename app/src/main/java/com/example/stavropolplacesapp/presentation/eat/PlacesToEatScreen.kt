package com.example.stavropolplacesapp.presentation.eat

import android.Manifest
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.stavropolplacesapp.eat.PlaceToEat
import com.example.stavropolplacesapp.eat.PlacesToEatRepository
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

data class PlaceToEatUi(
    val place: PlaceToEat,
    val distanceKm: Float?
)

@Composable
fun PlacesToEatScreen(
    onOpenDetails: (PlaceToEat) -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var isLoading by remember { mutableStateOf(true) }
    var places by remember { mutableStateOf<List<PlaceToEatUi>>(emptyList()) }
    var locationGranted by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    val categories = listOf("Все", "Бары/пабы", "Кафе", "Кофейни", "Рестораны")
    var selectedCategory by remember { mutableStateOf("Все") }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        locationGranted = (perms[Manifest.permission.ACCESS_FINE_LOCATION] == true)
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    LaunchedEffect(locationGranted) {
        isLoading = true
        val allPlaces = withContext(Dispatchers.IO) {
            PlacesToEatRepository.loadPlaces(context)
        }

        val location = if (locationGranted) {
            getLastLocation(fusedLocationClient)
        } else null

        places = allPlaces.map { place ->
            val distance = if (location != null) {
                val results = FloatArray(1)
                Location.distanceBetween(
                    location.latitude,
                    location.longitude,
                    place.coordinates.latitude,
                    place.coordinates.longitude,
                    results
                )
                results[0] / 1000f
            } else {
                null
            }
            PlaceToEatUi(place, distance)
        }
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val filtered = places.filter { item ->
        val category = detectCategory(item.place)
        val matchesCategory = selectedCategory == "Все" || category == selectedCategory
        val matchesQuery = query.isBlank() ||
            item.place.name.contains(query, ignoreCase = true) ||
            item.place.description.contains(query, ignoreCase = true)
        matchesCategory && matchesQuery
    }
    val finalList = filtered.sortedBy { it.distanceKm ?: Float.MAX_VALUE }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                Surface(
                    color = if (selectedCategory == category)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.clickable { selectedCategory = category }
                ) {
                    Text(
                        text = category,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = if (selectedCategory == category)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(finalList) { item ->
                PlaceToEatCard(
                    place = item.place,
                    distanceKm = item.distanceKm,
                    onClick = { onOpenDetails(item.place) }
                )
            }
        }
    }
}

@Composable
private fun PlaceToEatCard(
    place: PlaceToEat,
    distanceKm: Float?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                val imageUrl = place.photos.firstOrNull()
                AsyncImage(
                    model = imageUrl,
                    contentDescription = place.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart),
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = detectCategory(place),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = place.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            if (place.address.isNotBlank()) {
                Text(text = place.address, style = MaterialTheme.typography.bodySmall)
            }
            distanceKm?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = String.format("%.1f км", it), color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

private fun detectCategory(place: PlaceToEat): String {
    val text = "${place.name} ${place.description}".lowercase()
    return when {
        text.contains("коф") -> "Кофейни"
        text.contains("бар") || text.contains("паб") -> "Бары/пабы"
        text.contains("пицц") -> "Кафе"
        text.contains("суш") -> "Кафе"
        text.contains("ресторан") -> "Рестораны"
        text.contains("кафе") -> "Кафе"
        else -> "Где поесть"
    }
}

private suspend fun getLastLocation(
    client: com.google.android.gms.location.FusedLocationProviderClient
): android.location.Location? {
    return suspendCancellableCoroutine { cont ->
        client.lastLocation
            .addOnSuccessListener { loc -> cont.resume(loc) }
            .addOnFailureListener { cont.resume(null) }
    }
}
