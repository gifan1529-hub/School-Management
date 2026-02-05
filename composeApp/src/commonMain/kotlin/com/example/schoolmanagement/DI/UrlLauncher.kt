package com.example.schoolmanagement.DI

import androidx.compose.runtime.Composable

@Composable
expect fun rememberLauncher(): UrlLauncher

interface UrlLauncher {
    fun openUrl(url: String)
}