package com.mobileapp.kurs.data.dto.weather

data class WeatherResponse(
    val current: CurrentWeatherDto,
    val hourly: HourlyWeatherDto,
    val daily: DailyWeatherDto
)