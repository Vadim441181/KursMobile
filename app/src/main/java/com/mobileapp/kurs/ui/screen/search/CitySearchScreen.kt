package com.mobileapp.kurs.ui.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobileapp.kurs.R
import com.mobileapp.kurs.WeatherApplication
import com.mobileapp.kurs.data.remote.NetworkModule
import com.mobileapp.kurs.data.repository.WeatherRepository
import com.mobileapp.kurs.ui.components.SearchCityItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySearchScreen(
    onBackClick: () -> Unit,
    onCitySelected: () -> Unit
) {
    val paddingLarge = dimensionResource(R.dimen.padding_large)
    val paddingMedium = dimensionResource(R.dimen.padding_medium)

    val context = LocalContext.current.applicationContext as WeatherApplication

    val repository = WeatherRepository(
        cityDao = context.database.cityDao(),
        cityApiService = NetworkModule.cityApiService,
        weatherApiService = NetworkModule.weatherApiService
    )

    val viewModel: CitySearchViewModel = viewModel(
        factory = CitySearchViewModelFactory(repository)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.search_title))
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = paddingLarge),
            contentPadding = PaddingValues(vertical = paddingLarge),
            verticalArrangement = Arrangement.spacedBy(paddingMedium)
        ) {
            item {
                OutlinedTextField(
                    value = viewModel.query,
                    onValueChange = viewModel::onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.search_hint))
                    },
                    singleLine = true
                )
            }

            item {
                Button(
                    onClick = viewModel::searchCities,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.action_search))
                }
            }

            if (viewModel.isLoading) {
                item {
                    CircularProgressIndicator()
                }
            }

            viewModel.errorMessageResId?.let { errorResId ->
                item {
                    Text(
                        text = viewModel.errorMessageCode?.let { code ->
                            stringResource(errorResId, code)
                        } ?: stringResource(errorResId)
                    )
                }
            }

            items(viewModel.cities) { city ->
                SearchCityItem(
                    cityName = city.name,
                    subtitle = if (city.region.isBlank()) {
                        city.country
                    } else {
                        "${city.region}, ${city.country}"
                    },
                    onClick = {
                        viewModel.saveCity(city) {
                            onCitySelected()
                        }
                    }
                )
            }
        }
    }
}
