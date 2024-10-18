package com.example.stavropolplacesapp


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar // Правильный импорт
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.tabs.TabLayout
class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout
    private lateinit var cardViewPlaces: CardView
    private lateinit var cardViewRegion: CardView
    private lateinit var cardViewFamousPeople: CardView
    private lateinit var imageView: ImageView

    private val imageUrls = mutableListOf<String>()
    private var currentIndex = 0
    private val intervalMillis = 3000L // 3 секунды

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.view_pager)
        tabs = findViewById(R.id.tabs)
        cardViewPlaces = findViewById(R.id.card_view)
        cardViewFamousPeople = findViewById(R.id.card_view_famous_people)
        imageView = findViewById(R.id.main_image)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        // Добавляем URL-адреса изображений
        imageUrls.addAll(
            listOf(
                "https://extraguide.ru/images/pthumb/blog/2022/09-08-vwtdox-fontan-na-perspektivnom.cd9f7e96.jpg",
                "https://extraguide.ru/images/pthumb/blog/2022/09-08-2la5ks-sengileevskoe-vodohranilishche.cd9f7e96.jpg",
                "https://extraguide.ru/images/pthumb/blog/2022/09-08-qot1np-nemetskiy-most.cd9f7e96.jpg"
            )
        )

        // Запуск анимации смены изображений
        startImageAnimation()

        // Переход в PlacesActivity при нажатии на карточку "Места"
        cardViewPlaces.setOnClickListener {
            val intent = Intent(this, PlacesActivity::class.java)
            startActivity(intent)
        }

        // Переход в ZemlyakiActivity при нажатии на карточку "Земляки"
        cardViewFamousPeople.setOnClickListener {
            val intent = Intent(this, ZemlyakiActivity::class.java)
            startActivity(intent)
        }

        // В методе onCreate для MainActivity
        val cardRegion = findViewById<CardView>(R.id.card_view_region)
        cardRegion.setOnClickListener {
            val intent = Intent(this, RegionDetailActivity::class.java)
            startActivity(intent)
        }
        // Плитка для раздела "Афиша"
        val afishaCardView: CardView = findViewById(R.id.card_view_afisha)
        afishaCardView.setOnClickListener {
            val intent = Intent(this, AfishaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startImageAnimation() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                // Используем Glide для плавной смены изображений
                Glide.with(this@MainActivity)
                    .load(imageUrls[currentIndex])
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)

                // Увеличиваем индекс для следующего изображения
                currentIndex = (currentIndex + 1) % imageUrls.size

                // Повторяем каждые 3 секунды
                handler.postDelayed(this, intervalMillis)
            }
        }

        // Запускаем смену изображений
        handler.post(runnable)
    }
}











