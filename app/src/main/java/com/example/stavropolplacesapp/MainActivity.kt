package com.example.stavropolplacesapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

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

        // Initialize views
        cardViewPlaces = findViewById(R.id.card_view_places)
        cardViewFamousPeople = findViewById(R.id.card_view_famous_people)
        imageView = findViewById(R.id.main_image)

        // Add images to the image URL list
        imageUrls.addAll(
            listOf(
                "https://extraguide.ru/images/pthumb/blog/2022/09-08-vwtdox-fontan-na-perspektivnom.cd9f7e96.jpg",
                "https://extraguide.ru/images/pthumb/blog/2022/09-08-2la5ks-sengileevskoe-vodohranilishche.cd9f7e96.jpg",
                "https://extraguide.ru/images/pthumb/blog/2022/09-08-qot1np-nemetskiy-most.cd9f7e96.jpg"
            )
        )

        // Start image animation
        startImageAnimation()

        // Set up navigation between activities
        cardViewPlaces.setOnClickListener {
            val intent = Intent(this, PlacesActivity::class.java)
            startActivity(intent)
        }

        cardViewFamousPeople.setOnClickListener {
            val intent = Intent(this, ZemlyakiActivity::class.java)
            startActivity(intent)
        }

        // Card for Region
        val cardRegion = findViewById<CardView>(R.id.card_view_region)
        cardRegion.setOnClickListener {
            val intent = Intent(this, RegionDetailActivity::class.java)
            startActivity(intent)
        }

        // Card for Afisha
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
                // Use Glide to smoothly transition images
                Glide.with(this@MainActivity)
                    .load(imageUrls[currentIndex])
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)

                // Increment index for next image
                currentIndex = (currentIndex + 1) % imageUrls.size

                // Repeat every 3 seconds
                handler.postDelayed(this, intervalMillis)
            }
        }

        // Start image switching
        handler.post(runnable)
    }
}
