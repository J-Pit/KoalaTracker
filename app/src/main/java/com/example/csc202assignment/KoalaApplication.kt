package com.example.csc202assignment

import android.app.Application

class KoalaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KoalaRepository.initialize(this)
    }
}