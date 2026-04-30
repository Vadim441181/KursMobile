package com.mobileapp.kurs.data.dto.weather

data class DailyWeatherDto(
    val time: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val weather_code: List<Int>
)