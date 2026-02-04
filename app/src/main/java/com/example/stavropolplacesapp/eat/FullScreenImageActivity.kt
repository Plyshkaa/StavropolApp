package com.example.stavropolplacesapp.eat

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.stavropolplacesapp.R

class FullScreenImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        val photos = intent.getStringArrayExtra("photos") ?: arrayOf()
        val position = intent.getIntExtra("position", 0)

        val viewPager: ViewPager2 = findViewById(R.id.full_screen_view_pager)
        viewPager.adapter = PhotoPagerAdapter(this, photos)
        viewPager.currentItem = position

        // Настройка тулбара
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Настройки статус-бара
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.BLACK

    }
}
