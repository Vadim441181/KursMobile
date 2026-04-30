package com.mobileapp.kurs.data.remote

import com.mobileapp.kurs.data.dto.city.CityResult
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CityApiService {

    @GET("v1/city")
    suspend fun searchCities(
        @Header("X-Api-Key") apiKey: String,
        @Query("name") name: String
    ): List<CityResult>
}
