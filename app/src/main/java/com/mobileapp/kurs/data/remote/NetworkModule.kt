package com.mobileapp.kurs.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private val client = OkHttpClient.Builder().build()

    private fun retrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val cityApiService: CityApiService by lazy {
        retrofit("https://api.api-ninjas.com/")
            .create(CityApiService::class.java)
    }

    val weatherApiService: WeatherApiService by lazy {
        retrofit("https://api.open-meteo.com/")
            .create(WeatherApiService::class.java)
    }
}
