package com.mobileapp.kurs.data.repository

import com.mobileapp.kurs.BuildConfig
import com.mobileapp.kurs.data.dto.city.CityResult
import com.mobileapp.kurs.data.dto.weather.WeatherResponse
import com.mobileapp.kurs.data.local.CityDao
import com.mobileapp.kurs.data.local.CityEntity
import com.mobileapp.kurs.data.model.City
import com.mobileapp.kurs.data.remote.CityApiService
import com.mobileapp.kurs.data.remote.WeatherApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherRepository(
    private val cityDao: CityDao,
    private val cityApiService: CityApiService,
    private val weatherApiService: WeatherApiService
) {

    fun getSavedCities(): Flow<List<City>> {
        return cityDao.getAllCities().map { cityEntities ->
            cityEntities.map { entity ->
                entity.toCity()
            }
        }
    }

    suspend fun saveCity(city: City) {
        cityDao.insertCity(city.toEntity())
    }

    suspend fun deleteCity(city: City) {
        cityDao.deleteCityById(city.id)
    }

    suspend fun isCitySaved(city: City): Boolean {
        return cityDao.isCitySaved(
            name = city.name,
            latitude = city.latitude,
            longitude = city.longitude
        )
    }

    suspend fun searchCitiesByName(query: String): List<City> {
        if (query.isBlank()) return emptyList()
        if (BuildConfig.API_NINJAS_KEY.isBlank()) {
            throw MissingApiKeyException()
        }

        return cityApiService
            .searchCities(
                apiKey = BuildConfig.API_NINJAS_KEY,
                name = query.trim()
            )
            .sortedByDescending { it.population ?: 0 }
            .map { dto ->
                dto.toCity()
            }
    }

    suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponse {
        return weatherApiService.getForecast(
            latitude = latitude,
            longitude = longitude
        )
    }
}

private fun CityEntity.toCity(): City {
    return City(
        id = id,
        name = name,
        country = country,
        region = region,
        latitude = latitude,
        longitude = longitude
    )
}

private fun City.toEntity(): CityEntity {
    return CityEntity(
        id = id,
        name = name,
        country = country,
        region = region,
        latitude = latitude,
        longitude = longitude
    )
}

private fun CityResult.toCity(): City {
    return City(
        id = 0,
        name = name,
        country = country,
        region = "",
        latitude = latitude,
        longitude = longitude
    )
}
