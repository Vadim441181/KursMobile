package com.mobileapp.kurs.ui.screen.weather

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobileapp.kurs.R
import com.mobileapp.kurs.WeatherApplication
import com.mobileapp.kurs.data.remote.NetworkModule
import com.mobileapp.kurs.data.repository.WeatherRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    cityName: String,
    latitude: Double,
    longitude: Double,
    onBackClick: () -> Unit
) {
    val paddingXsmall = dimensionResource(R.dimen.padding_xsmall)
    val paddingSmall = dimensionResource(R.dimen.padding_small)
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val paddingLarge = dimensionResource(R.dimen.padding_large)
    val hourlyItemWidth = dimensionResource(R.dimen.hourly_item_width)

    val context = LocalContext.current.applicationContext as WeatherApplication

    val repository = WeatherRepository(
        cityDao = context.database.cityDao(),
        cityApiService = NetworkModule.cityApiService,
        weatherApiService = NetworkModule.weatherApiService
    )

    val viewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory(repository)
    )

    LaunchedEffect(latitude, longitude) {
        viewModel.loadWeather(latitude, longitude)
    }

    val weather = viewModel.weather

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = cityName)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                },
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) { innerPadding ->

        when {
            viewModel.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            viewModel.errorResId != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(paddingLarge),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(viewModel.errorResId!!),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            weather != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(paddingLarge),
                    verticalArrangement = Arrangement.spacedBy(paddingLarge)
                ) {
                    item {
                        CurrentWeatherBlock(
                            temperature = weather.current.temperature_2m,
                            humidity = weather.current.relative_humidity_2m,
                            windSpeed = weather.current.wind_speed_10m,
                            weatherCode = weather.current.weather_code,
                            paddingLarge = paddingLarge,
                            paddingSmall = paddingSmall
                        )
                    }

                    item {
                        ForecastSectionTitle(
                            title = stringResource(R.string.details_hourly_forecast)
                        )
                    }

                    item {
                        HourlyForecastBlock(
                            times = weather.hourly.time,
                            temperatures = weather.hourly.temperature_2m,
                            weatherCodes = weather.hourly.weather_code,
                            paddingMedium = paddingMedium,
                            paddingXsmall = paddingXsmall,
                            paddingSmall = paddingSmall,
                            hourlyItemWidth = hourlyItemWidth
                        )
                    }

                    item {
                        ForecastSectionTitle(
                            title = stringResource(R.string.details_daily_forecast)
                        )
                    }

                    item {
                        DailyForecastBlock(
                            dates = weather.daily.time,
                            maxTemperatures = weather.daily.temperature_2m_max,
                            minTemperatures = weather.daily.temperature_2m_min,
                            weatherCodes = weather.daily.weather_code,
                            paddingLarge = paddingLarge,
                            paddingMedium = paddingMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentWeatherBlock(
    temperature: Double,
    humidity: Int,
    windSpeed: Double,
    weatherCode: Int,
    paddingLarge: androidx.compose.ui.unit.Dp,
    paddingSmall: androidx.compose.ui.unit.Dp
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(paddingLarge),
            verticalArrangement = Arrangement.spacedBy(paddingSmall)
        ) {
            Text(
                text = stringResource(R.string.current_weather_title),
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = stringResource(
                    id = R.string.weather_temperature,
                    temperature.toInt()
                ),
                style = MaterialTheme.typography.displaySmall
            )

            Text(
                text = stringResource(weatherCodeToTextRes(weatherCode)),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = stringResource(
                    id = R.string.weather_humidity,
                    humidity
                ),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(
                    id = R.string.weather_wind,
                    windSpeed
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ForecastSectionTitle(
    title: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
private fun HourlyForecastBlock(
    times: List<String>,
    temperatures: List<Double>,
    weatherCodes: List<Int>,
    paddingMedium: androidx.compose.ui.unit.Dp,
    paddingXsmall: androidx.compose.ui.unit.Dp,
    paddingSmall: androidx.compose.ui.unit.Dp,
    hourlyItemWidth: androidx.compose.ui.unit.Dp
) {
    val count = minOf(times.size, temperatures.size, weatherCodes.size, 24)
    val itemsList = List(count) { index ->
        Triple(times[index], temperatures[index], weatherCodes[index])
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(paddingMedium),
        contentPadding = PaddingValues(horizontal = paddingXsmall)
    ) {
        items(itemsList) { item ->
            HourItem(
                time = item.first,
                temperature = item.second,
                weatherCode = item.third,
                paddingXsmall = paddingXsmall,
                paddingSmall = paddingSmall,
                paddingMedium = paddingMedium,
                hourlyItemWidth = hourlyItemWidth
            )
        }
    }
}

@Composable
private fun HourItem(
    time: String,
    temperature: Double,
    weatherCode: Int,
    paddingXsmall: androidx.compose.ui.unit.Dp,
    paddingSmall: androidx.compose.ui.unit.Dp,
    paddingMedium: androidx.compose.ui.unit.Dp,
    hourlyItemWidth: androidx.compose.ui.unit.Dp
) {
    Card(
        modifier = Modifier.width(hourlyItemWidth),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = paddingSmall,
                vertical = paddingMedium
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(paddingXsmall)
        ) {
            Text(
                text = formatHour(time),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(
                    id = R.string.weather_temperature_short,
                    temperature.toInt()
                ),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                        text = stringResource(weatherCodeToTextRes(weatherCode)),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun DailyForecastBlock(
    dates: List<String>,
    maxTemperatures: List<Double>,
    minTemperatures: List<Double>,
    weatherCodes: List<Int>,
    paddingLarge: androidx.compose.ui.unit.Dp,
    paddingMedium: androidx.compose.ui.unit.Dp
) {
    val count = minOf(
        dates.size,
        maxTemperatures.size,
        minTemperatures.size,
        weatherCodes.size
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(count) { index ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = paddingLarge, vertical = paddingMedium)
                ) {
                    Text(
                        text = formatDate(dates[index]),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = stringResource(
                            id = R.string.weather_temperature_range,
                            maxTemperatures[index].toInt(),
                            minTemperatures[index].toInt()
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = stringResource(weatherCodeToTextRes(weatherCodes[index])),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (index != count - 1) {
                    HorizontalDivider()
                }
            }
        }
    }
}

private fun formatHour(time: String): String {
    return if (time.length >= 16) {
        time.substring(11, 16)
    } else {
        time
    }
}

private fun formatDate(date: String): String {
    return date
}

@StringRes
private fun weatherCodeToTextRes(code: Int): Int {
    return when (code) {
        0 -> R.string.weather_code_clear
        1 -> R.string.weather_code_mostly_clear
        2 -> R.string.weather_code_partly_cloudy
        3 -> R.string.weather_code_overcast
        45, 48 -> R.string.weather_code_fog
        51, 53, 55 -> R.string.weather_code_drizzle
        61, 63, 65 -> R.string.weather_code_rain
        71, 73, 75 -> R.string.weather_code_snow
        80, 81, 82 -> R.string.weather_code_showers
        95 -> R.string.weather_code_thunderstorm
        else -> R.string.weather_code_unknown
    }
}
