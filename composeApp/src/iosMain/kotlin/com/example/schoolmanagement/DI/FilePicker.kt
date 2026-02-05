package com.example.schoolmanagement.DI

import com.darkrockstudios.libraries.mpfilepicker.MPFile
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfURL

@OptIn(ExperimentalForeignApi::class)
actual suspend fun MPFile<*>.readFileBytes(): ByteArray? {
    val url = this.platformFile as? platform.Foundation.NSURL ?: return null
    val data = NSData.dataWithContentsOfURL(url) ?: return null

    val bytes = ByteArray(data.length.toInt())
    if (data.length > 0u) {
        bytes.usePinned { pinned ->
            platform.posix.memcpy(pinned.addressOf(0), data.bytes, data.length)
        }
    }
    return bytes
}