package com.example.schoolmanagement.DI

import android.widget.Toast
import com.example.schoolmanagement.MyApplication

actual class ToastHelper {
    actual fun Toast(message: String) {
        Toast.makeText(MyApplication.appContext, message, Toast.LENGTH_SHORT).show()
    }
}