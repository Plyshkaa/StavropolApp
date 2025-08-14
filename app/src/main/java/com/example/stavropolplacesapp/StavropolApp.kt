package com.example.stavropolplacesapp

import android.app.Application

class StavropolApp : Application() {
    
    companion object {
        lateinit var instance: StavropolApp
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
