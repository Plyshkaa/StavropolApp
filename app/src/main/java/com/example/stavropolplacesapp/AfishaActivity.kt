package com.example.stavropolplacesapp

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class AfishaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_afisha)

        // Находим WebView и контейнер с изображением
        val webView: WebView = findViewById(R.id.afishaWebView)
        val progressBarContainer: View = findViewById(R.id.progress_container)

        // Включаем JavaScript
        webView.settings.javaScriptEnabled = true

        // Устанавливаем WebViewClient для открытия страниц внутри WebView
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                // Когда страница загружена, скрываем картинку загрузки и показываем WebView
                progressBarContainer.visibility = View.GONE
                webView.visibility = View.VISIBLE
            }
        }

        // Загружаем URL
        webView.loadUrl("https://afisha.yandex.ru/stavropol")
    }
}
