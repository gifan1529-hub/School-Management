package com.example.schoolmanagement.DI

import android.content.Context
import android.net.Uri
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject

actual suspend fun MPFile<*>.readFileBytes(): ByteArray? {
    val uri = this.platformFile as? Uri ?: return null

    val context: Context = org.koin.core.context.GlobalContext.get().get()

    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.readBytes()
        }
    } catch (e: Exception) {
        null
    }
}