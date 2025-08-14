package com.example.stavropolplacesapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.stavropolplacesapp.about.AboutScreen
import com.example.stavropolplacesapp.afisha.AfishaActivity
import com.example.stavropolplacesapp.favorites.FavoritesActivity
import com.example.stavropolplacesapp.databinding.ActivityMainBinding
import com.example.stavropolplacesapp.eat.PlacesToEatActivity
import com.example.stavropolplacesapp.famous_people.ZemlyakiActivity
import com.example.stavropolplacesapp.places.PlacesActivity
import com.example.stavropolplacesapp.region.RegionDetailActivity
import com.example.stavropolplacesapp.utils.PermissionUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = Constants.TAG_MAIN_ACTIVITY
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        checkLocationServiceAndPermission()
        setupNavigation()
        setupCardListeners()
    }

    private fun setupUI() {
        // Устанавливаем фрагмент по умолчанию при запуске активности
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            replaceFragment(HomeFragment())
        }

        // Настройка прозрачного статус-бара
        window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or 
                                          View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or 
                                          View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setupNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_places -> {
                    startActivity(Intent(this, PlacesActivity::class.java))
                    true
                }
                R.id.nav_favorites -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                R.id.nav_about -> {
                    startActivity(Intent(this, AboutScreen::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun checkLocationServiceAndPermission() {
        if (!PermissionUtils.isLocationEnabled(this)) {
            PermissionUtils.showLocationDialog(
                this,
                onPositiveClick = { PermissionUtils.openLocationSettings(this) },
                onNegativeClick = { Log.w(TAG, "Геолокация не включена пользователем") }
            )
        } else {
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        if (!PermissionUtils.hasLocationPermission(this)) {
            Log.d(TAG, "Запрос разрешения на доступ к местоположению")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Constants.LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            onPermissionGranted()
        }
    }

    private fun onPermissionGranted() {
        Log.d(TAG, "Разрешение на доступ к местоположению получено")
        // Логика для работы с местоположением
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted()
            } else {
                Log.d(TAG, "Пользователь отклонил запрос на доступ к местоположению")
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setupCardListeners() {
        with(binding) {
            cardViewPlaces.setOnClickListener {
                startActivity(Intent(this@MainActivity, PlacesActivity::class.java))
            }

            cardViewFamousPeople.setOnClickListener {
                startActivity(Intent(this@MainActivity, ZemlyakiActivity::class.java))
            }

            cardViewRegion.setOnClickListener {
                startActivity(Intent(this@MainActivity, RegionDetailActivity::class.java))
            }

            cardViewAfisha.setOnClickListener {
                startActivity(Intent(this@MainActivity, AfishaActivity::class.java))
            }

            cardViewWhereToEat.setOnClickListener {
                startActivity(Intent(this@MainActivity, PlacesToEatActivity::class.java))
            }
        }
    }
}
