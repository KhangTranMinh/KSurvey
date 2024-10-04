package com.ktm.ksurvey.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(
    onNavigateToThankYouScreen: () -> Unit,
) {
    Column {
        Text(text = "HomeScreen")
        Button(onClick = onNavigateToThankYouScreen) {
            Text(text = "To Thank You screen")
        }
    }
}