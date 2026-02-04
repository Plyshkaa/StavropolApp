package com.example.stavropolplacesapp.presentation.eat

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.stavropolplacesapp.eat.WorkingDays
import com.example.stavropolplacesapp.eat.FullScreenImageActivity
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale

data class PlaceEatDetailUi(
    val name: String,
    val description: String,
    val address: String,
    val phone: String,
    val coordinates: String,
    val photos: List<String>,
    val workingHours: WorkingDays?
)

@Composable
fun PlaceEatDetailScreen(data: PlaceEatDetailUi) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (data.photos.isNotEmpty()) {
            LazyRow(
                contentPadding = PaddingValues(0.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(data.photos) { index, photo ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        AsyncImage(
                            model = photo,
                            contentDescription = data.name,
                            modifier = Modifier
                                .size(width = 280.dp, height = 180.dp)
                                .clickable {
                                    val intent = Intent(context, FullScreenImageActivity::class.java).apply {
                                        putExtra("photos", data.photos.toTypedArray())
                                        putExtra("position", index)
                                    }
                                    context.startActivity(intent)
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (data.description.isNotBlank()) {
            Text(text = data.description)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Информация о месте", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                InfoRow(title = "Адрес", value = data.address)
                Spacer(modifier = Modifier.height(8.dp))

                InfoRow(
                    title = "Телефон",
                    value = data.phone,
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${data.phone}"))
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                InfoRow(
                    title = "Координаты",
                    value = data.coordinates,
                    onClick = {
                        val uri = Uri.parse("geo:${data.coordinates}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(intent)
                    }
                )

                data.workingHours?.let { hours ->
                    var expanded by remember { mutableStateOf(false) }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = !expanded },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Часы работы", fontWeight = FontWeight.Bold)
                        Text(
                            text = if (expanded) "Скрыть" else "Показать все",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    WorkingHours(hours = hours, expanded = expanded)
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    title: String,
    value: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = value, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun WorkingHours(hours: WorkingDays, expanded: Boolean) {
    val items = listOf(
        "Понедельник" to hours.monday,
        "Вторник" to hours.tuesday,
        "Среда" to hours.wednesday,
        "Четверг" to hours.thursday,
        "Пятница" to hours.friday,
        "Суббота" to hours.saturday,
        "Воскресенье" to hours.sunday
    )
    val today = LocalDate.now().dayOfWeek
    val todayLabel = when (today) {
        DayOfWeek.MONDAY -> "Понедельник"
        DayOfWeek.TUESDAY -> "Вторник"
        DayOfWeek.WEDNESDAY -> "Среда"
        DayOfWeek.THURSDAY -> "Четверг"
        DayOfWeek.FRIDAY -> "Пятница"
        DayOfWeek.SATURDAY -> "Суббота"
        DayOfWeek.SUNDAY -> "Воскресенье"
    }
    val visibleItems = if (expanded) items else items.filter { it.first == todayLabel }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        visibleItems.forEach { (day, value) ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = day.uppercase(Locale.getDefault()))
                Text(text = value)
            }
        }
    }
}
