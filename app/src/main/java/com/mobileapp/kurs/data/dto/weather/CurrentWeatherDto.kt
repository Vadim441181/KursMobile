package com.mobileapp.kurs.data.dto.weather

data class CurrentWeatherDto(
    val temperature_2m: Double,
    val weather_code: Int,
    val relative_humidity_2m: Int,
    val wind_speed_10m: Double
)