package com.example.stavropolplacesapp.afisha

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.stavropolplacesapp.R

class AfishaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_afisha)

        // Инициализация Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Добавляем кнопку назад
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.black)) // Устанавливаем чёрный цвет
        // Устанавливаем заголовок в центр
        supportActionBar?.title = ""
        val titleTextView: TextView = findViewById(R.id.toolbar_title)
        titleTextView.text = "Афиша"
        // Добавляем действие при нажатии на кнопку возврата
        toolbar.setNavigationOnClickListener {
            onBackPressed() // Возврат на предыдущий экран
        }


        // Прозрачный статус-бар с видимыми иконками
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        // Используем светлый статус-бар для видимых иконок (черные иконки)
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

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
