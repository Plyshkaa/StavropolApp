package com.example.stavropolplacesapp


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout
    private lateinit var cardView: CardView
    private lateinit var imageView: ImageView

    private val imageUrls = mutableListOf<String>()
    private var currentIndex = 0
    private val intervalMillis = 3000L // 3 секунды

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.view_pager)
        tabs = findViewById(R.id.tabs)
        cardView = findViewById(R.id.card_view)
        imageView = findViewById(R.id.main_image)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        // Добавляем URL-адреса изображений из объектов places
        imageUrls.addAll(
            listOf(
                "https://extraguide.ru/images/pthumb/blog/2022/09-08-vwtdox-fontan-na-perspektivnom.cd9f7e96.jpg",
                "https://extraguide.ru/images/pthumb/blog/2022/09-08-2la5ks-sengileevskoe-vodohranilishche.cd9f7e96.jpg",
                "https://extraguide.ru/images/pthumb/blog/2022/09-08-qot1np-nemetskiy-most.cd9f7e96.jpg",
                "https://extraguide.ru/images/pthumb/blog/2022/09-07-et8039-park-pobedy.cd9f7e96.jpg",
                "https://extraguide.ru/images/pthumb/blog/2022/09-07-et8039-park-pobedy.cd9f7e96.jpg",
                "https://extraguide.ru/images/pthumb/blog/2022/09-08-vhga90-stavropolskiy-zoopark.cd9f7e96.jpg",
                // Добавьте остальные URL из вашего JSON
            )
        )

        // Запуск анимации смены изображений
        startImageAnimation()

        // Переход в PlacesActivity при нажатии на карточку "Места"
        cardView.setOnClickListener {
            val intent = Intent(this, PlacesActivity::class.java)
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

                // Увеличиваем индекс, чтобы перейти к следующему изображению
                currentIndex = (currentIndex + 1) % imageUrls.size

                // Повторяем каждую 3 секунду
                handler.postDelayed(this, intervalMillis)
            }
        }

        // Запускаем смену изображений
        handler.post(runnable)
    }
}









