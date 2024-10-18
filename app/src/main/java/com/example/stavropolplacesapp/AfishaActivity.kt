package com.example.stavropolplacesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebView
import android.webkit.WebViewClient

class AfishaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_afisha)

        // Устанавливаем WebView для отображения афиши
        val webView: WebView = findViewById(R.id.afishaWebView)
        webView.webViewClient = WebViewClient() // Открывать страницы внутри WebView
        webView.settings.javaScriptEnabled = true // Включить поддержку JavaScript
        webView.loadUrl("https://afisha.yandex.ru/stavropol") // Загрузить афишу
    }
}
