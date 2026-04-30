package com.mobileapp.kurs

import android.app.Application
import androidx.room.Room
import com.mobileapp.kurs.data.local.AppDatabase

class WeatherApplication : Application() {

    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "weather_database"
        ).build()
    }
}