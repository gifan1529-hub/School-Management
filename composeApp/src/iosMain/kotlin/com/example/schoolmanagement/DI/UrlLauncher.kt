package com.example.schoolmanagement.DI

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication


@Composable
actual fun rememberLauncher(): UrlLauncher {
    return remember {
        object : UrlLauncher {
            override fun openUrl(url: String) {
                val nsUrl = NSURL.URLWithString(url)
                if (nsUrl != null) {
                    UIApplication.sharedApplication.openURL(nsUrl)
                }
            }
        }
    }
}