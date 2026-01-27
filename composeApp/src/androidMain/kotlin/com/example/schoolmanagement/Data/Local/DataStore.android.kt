@file:JvmName("DataStoreAndroid")
package com.example.schoolmanagement.Data.Local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

actual fun createDataStore(context: Any): DataStore<Preferences> {
    val ctx = context as Context
    return PreferenceDataStoreFactory.createWithPath (
        produceFile = {
            ctx.filesDir.resolve(DATASTORE_FILE_NAME).absolutePath.toPath() }
    )
}