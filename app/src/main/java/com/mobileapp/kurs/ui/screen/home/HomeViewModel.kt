package com.mobileapp.kurs.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobileapp.kurs.data.model.City
import com.mobileapp.kurs.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities.asStateFlow()

    init {
        loadCities()
    }

    private fun loadCities() {
        viewModelScope.launch {
            repository.getSavedCities().collect { savedCities ->
                _cities.value = savedCities
            }
        }
    }

    fun deleteCity(city: City) {
        viewModelScope.launch {
            repository.deleteCity(city)
        }
    }
}
