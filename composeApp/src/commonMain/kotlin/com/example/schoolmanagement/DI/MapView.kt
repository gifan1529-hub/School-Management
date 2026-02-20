package com.example.schoolmanagement.DI

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MapView (
    modifier: Modifier,
    latitude: Double, longitude: Double,
    title: String,
    radius: Double = 300.0
)