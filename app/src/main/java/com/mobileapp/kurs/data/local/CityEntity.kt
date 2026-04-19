package com.mobileapp.kurs.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cities",
    indices = [
        Index(value = ["name", "latitude", "longitude"], unique = true)
    ]
)
data class CityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val country: String,
    val region: String,
    val latitude: Double,
    val longitude: Double
)