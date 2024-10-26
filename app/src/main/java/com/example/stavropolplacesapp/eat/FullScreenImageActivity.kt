package com.example.stavropolplacesapp.eat

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.stavropolplacesapp.R

class FullScreenImageActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        val photos = intent.getStringArrayExtra("photos") ?: arrayOf()
        val position = intent.getIntExtra("position", 0)

        val viewPager: ViewPager2 = findViewById(R.id.full_screen_view_pager)
        viewPager.adapter = PhotoPagerAdapter(this, photos)
        viewPager.currentItem = position


        // Получаем URL изображения из Intent
        val imageUrl = intent.getStringExtra("photoUrl")
        val imageView: ImageView = findViewById(R.id.fullscreen_image_view)

        // Загружаем изображение с помощью Glide
        Glide.with(this)
            .load(imageUrl)
            .fitCenter()  // Настройка, чтобы изображение занимало весь экран
            .into(imageView)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        // Устанавливаем иконку назад и цвет
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setTint(
            ContextCompat.getColor(this, R.color.black)
        )

        // Добавляем действие при нажатии на кнопку возврата
        toolbar.setNavigationOnClickListener {
            onBackPressed() // Возврат на предыдущий экран
        }

        // Прозрачный статус-бар с видимыми иконками
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        // Используем светлый статус-бар для видимых иконок (черные иконки)
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


}

}
