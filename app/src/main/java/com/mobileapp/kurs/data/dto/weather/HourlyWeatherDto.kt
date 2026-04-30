package com.mobileapp.kurs.data.dto.weather

data class HourlyWeatherDto(
    val time: List<String>,
    val temperature_2m: List<Double>,
    val weather_code: List<Int>
)