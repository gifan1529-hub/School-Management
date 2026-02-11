package com.example.schoolmanagement.UI.Component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import kotlinx.coroutines.delay

enum class ToastType {
    SUCCESS, ERROR, INFO
}
@Composable
fun CustomToast(
    message: String,
    isVisible: Boolean,
    type: ToastType = ToastType.INFO,
    onDismiss: () -> Unit
) {
    val poppins = getPoppinsFontFamily()

    val (backgroundColor, icon, iconColor) = when (type) {
        ToastType.SUCCESS -> Triple(Color(0xFFE8F5E9), Icons.Default.CheckCircle, Color(0xFF4CAF50))
        ToastType.ERROR -> Triple(Color(0xFFFFEBEE), Icons.Default.Error, Color(0xFFF44336))
        ToastType.INFO -> Triple(Color(0xFFE3F2FD), Icons.Default.Info, Color(0xFF0066FF))
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 4.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(message, color = Color.Black, fontSize = 14.sp)
                }
            }
        }
    }

    // Auto-hide setelah 3 detik
    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(3000)
            onDismiss()
        }
    }
}