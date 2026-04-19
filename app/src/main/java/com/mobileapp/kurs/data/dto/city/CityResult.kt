package com.mobileapp.kurs.data.dto.city

data class CityResult(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val population: Int?
)
