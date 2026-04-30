package com.mobileapp.kurs.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.mobileapp.kurs.R

@Composable
fun SearchCityItem(
    cityName: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val paddingLarge = dimensionResource(R.dimen.padding_large)
    val paddingXsmall = dimensionResource(R.dimen.padding_xsmall)

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(paddingLarge),
            verticalArrangement = Arrangement.spacedBy(paddingXsmall)
        ) {
            Text(
                text = cityName,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
