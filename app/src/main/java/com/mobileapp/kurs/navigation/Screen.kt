package com.mobileapp.kurs.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Weather : Screen("weather/{cityName}/{latitude}/{longitude}") {
        fun createRoute(
            cityName: String,
            latitude: Double,
            longitude: Double
        ): String {
            return "weather/$cityName/$latitude/$longitude"
        }
    }
}