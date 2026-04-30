package com.mobileapp.kurs.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobileapp.kurs.R
import com.mobileapp.kurs.WeatherApplication
import com.mobileapp.kurs.data.model.City
import com.mobileapp.kurs.data.remote.NetworkModule
import com.mobileapp.kurs.data.repository.WeatherRepository
import com.mobileapp.kurs.ui.components.CityCard
import com.mobileapp.kurs.ui.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddCityClick: () -> Unit,
    onCityClick: (City) -> Unit
) {
    val paddingLarge = dimensionResource(R.dimen.padding_large)
    val paddingMedium = dimensionResource(R.dimen.padding_medium)

    val context = LocalContext.current.applicationContext as WeatherApplication

    val repository = WeatherRepository(
        cityDao = context.database.cityDao(),
        cityApiService = NetworkModule.cityApiService,
        weatherApiService = NetworkModule.weatherApiService
    )

    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(repository)
    )

    val cities by viewModel.cities.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.home_title))
                },
                modifier = Modifier.statusBarsPadding(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCityClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.action_add_city)
                )
            }
        }
    ) { innerPadding ->
        if (cities.isEmpty()) {
            EmptyState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                title = stringResource(R.string.empty_city_list),
                description = stringResource(R.string.home_placeholder),
                buttonText = stringResource(R.string.action_open_search),
                onButtonClick = onAddCityClick
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(paddingLarge),
                verticalArrangement = Arrangement.spacedBy(paddingMedium)
            ) {
                items(cities) { city ->
                    CityCard(
                        cityName = city.name,
                        subtitle = if (city.region.isBlank()) {
                            city.country
                        } else {
                            "${city.region}, ${city.country}"
                        },
                        onClick = {
                            onCityClick(city)
                        },
                        onDeleteClick = {
                            viewModel.deleteCity(city)
                        }
                    )
                }
            }
        }
    }
}
