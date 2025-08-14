package com.example.stavropolplacesapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import android.content.Intent

object PermissionUtils {
    
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    
    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    fun showLocationDialog(
        context: Context,
        onPositiveClick: () -> Unit,
        onNegativeClick: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle("Включить геолокацию")
            .setMessage("Для работы приложения необходимо включить геолокацию.")
            .setPositiveButton("Включить") { _, _ ->
                onPositiveClick()
            }
            .setNegativeButton("Отмена") { _, _ ->
                onNegativeClick()
            }
            .setCancelable(false)
            .show()
    }
    
    fun openLocationSettings(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }
}
