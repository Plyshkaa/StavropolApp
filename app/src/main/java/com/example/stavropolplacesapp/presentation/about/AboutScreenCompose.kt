package com.example.stavropolplacesapp.presentation.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.stavropolplacesapp.BuildConfig
import com.example.stavropolplacesapp.R

@Composable
fun AboutScreenCompose() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.mipmap.logo_app),
            contentDescription = "Логотип",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ставротека",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Версия ${BuildConfig.VERSION_NAME}",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Ставротека — твой гид по Ставропольскому краю!\nСтавротека — это удобное и полезное приложение для жителей и гостей Ставропольского края",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Разработчик: Илья Черняев")
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ilya.cherhyaev31@yandex.ru",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:Ilya.cherhyaev31@yandex.ru")
                }
                context.startActivity(intent)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Следи за новостями", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            SocialIcon(
                iconRes = R.drawable.rutube,
                label = "YouTube",
                onClick = { openUrl(context, "https://rutube.ru/u/svoe/") }
            )
            SocialIcon(
                iconRes = R.drawable.vk,
                label = "ВКонтакте",
                onClick = { openUrl(context, "https://vk.com/stav_region") }
            )
            SocialIcon(
                iconRes = R.drawable.telegram,
                label = "Telegram",
                onClick = { openUrl(context, "https://t.me/s/stavropolye_tv") }
            )
        }
    }
}

@Composable
private fun SocialIcon(
    iconRes: Int,
    label: String,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier
                .size(48.dp)
                .clickable { onClick() }
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label)
    }
}

private fun openUrl(context: android.content.Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
