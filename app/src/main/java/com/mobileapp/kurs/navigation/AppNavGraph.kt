package com.mobileapp.kurs.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mobileapp.kurs.ui.screen.home.HomeScreen
import com.mobileapp.kurs.ui.screen.search.CitySearchScreen
import com.mobileapp.kurs.ui.screen.weather.WeatherScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onAddCityClick = {
                    navController.navigate(Screen.Search.route)
                },
                onCityClick = { city ->
                    navController.navigate(
                        Screen.Weather.createRoute(
                            cityName = city.name,
                            latitude = city.latitude,
                            longitude = city.longitude
                        )
                    )
                }
            )
        }

        composable(route = Screen.Search.route) {
            CitySearchScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onCitySelected = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Weather.route,
            arguments = listOf(
                navArgument("cityName") {
                    type = NavType.StringType
                },
                navArgument("latitude") {
                    type = NavType.StringType
                },
                navArgument("longitude") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("cityName").orEmpty()
            val latitude = backStackEntry.arguments?.getString("latitude")?.toDoubleOrNull() ?: 0.0
            val longitude = backStackEntry.arguments?.getString("longitude")?.toDoubleOrNull() ?: 0.0

            WeatherScreen(
                cityName = cityName,
                latitude = latitude,
                longitude = longitude,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}