package com.mobileapp.kurs.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.mobileapp.kurs.R

@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    val paddingLarge = dimensionResource(R.dimen.padding_large)
    val paddingXlarge = dimensionResource(R.dimen.padding_xlarge)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingXlarge),
        verticalArrangement = Arrangement.spacedBy(paddingLarge)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge
        )

        Button(onClick = onButtonClick) {
            Text(text = buttonText)
        }
    }
}
