package com.mobileapp.kurs.ui.screen.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobileapp.kurs.R
import com.mobileapp.kurs.data.model.City
import com.mobileapp.kurs.data.repository.MissingApiKeyException
import com.mobileapp.kurs.data.repository.WeatherRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CitySearchViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    var query by mutableStateOf("")
        private set

    var cities by mutableStateOf<List<City>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessageResId by mutableStateOf<Int?>(null)
        private set

    var errorMessageCode by mutableStateOf<Int?>(null)
        private set

    fun onQueryChange(newValue: String) {
        query = newValue
    }

    fun searchCities() {
        if (query.isBlank()) {
            cities = emptyList()
            errorMessageResId = null
            errorMessageCode = null
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessageResId = null
            errorMessageCode = null

            try {
                cities = repository.searchCitiesByName(query.trim())

                if (cities.isEmpty()) {
                    errorMessageResId = R.string.error_city_not_found
                }
            } catch (_: MissingApiKeyException) {
                errorMessageResId = R.string.error_city_search_missing_api_key
                cities = emptyList()
            } catch (e: HttpException) {
                errorMessageResId = R.string.error_city_search_server
                errorMessageCode = e.code()
                cities = emptyList()
            } catch (_: IOException) {
                errorMessageResId = R.string.error_city_search_network
                cities = emptyList()
            } catch (_: Exception) {
                errorMessageResId = R.string.error_city_search_generic
                cities = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    fun saveCity(city: City, onSaved: () -> Unit) {
        viewModelScope.launch {
            val isSaved = repository.isCitySaved(city)

            if (!isSaved) {
                repository.saveCity(city)
            }

            onSaved()
        }
    }
}
