package com.mobileapp.kurs.ui.screen.weather

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobileapp.kurs.R
import com.mobileapp.kurs.data.dto.weather.WeatherResponse
import com.mobileapp.kurs.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    var weather by mutableStateOf<WeatherResponse?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorResId by mutableStateOf<Int?>(null)
        private set

    fun loadWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            isLoading = true
            errorResId = null

            try {
                weather = repository.getWeather(
                    latitude = latitude,
                    longitude = longitude
                )
            } catch (_: Exception) {
                errorResId = R.string.error_weather_load
            } finally {
                isLoading = false
            }
        }
    }
}
