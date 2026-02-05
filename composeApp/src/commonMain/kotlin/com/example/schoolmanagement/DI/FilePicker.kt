package com.example.schoolmanagement.DI

import com.darkrockstudios.libraries.mpfilepicker.MPFile

expect suspend fun MPFile<*>.readFileBytes(): ByteArray?