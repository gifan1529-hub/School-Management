package com.example.schoolmanagement.DI

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberLauncher(): UrlLauncher {
    val context = LocalContext.current
    return remember {
        object : UrlLauncher {
            override fun openUrl(url: String) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                // Menambahkan header bypass Ngrok (Hanya bekerja jika browser mendukung)
//                intent.putExtra("com.android.browser.headers", android.os.Bundle().apply {
//                    putString("ngrok-skip-browser-warning", "true")
//                })
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }
}