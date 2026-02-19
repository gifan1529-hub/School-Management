package com.example.schoolmanagement.Data.Local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.schoolmanagement.Domain.Model.StudentMiniData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import com.example.schoolmanagement.Domain.Model.UserDetails

class PrefsManager (
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val KEY_USER_ID = intPreferencesKey("user_id")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val KEY_EMAIL = stringPreferencesKey("key_email")
        private val KEY_TOKEN = stringPreferencesKey("key_token")
        private val KEY_PASSWORD = stringPreferencesKey("key_password")
        private val KEY_NAME = stringPreferencesKey("user_name")
        private val KEY_ROLE = stringPreferencesKey("user_role")
        private val IS_ALREADY_ABSEN = booleanPreferencesKey("is_absen")
        private val LAST_ABSEN_DATE = stringPreferencesKey("last_absen")
        private val NISN = stringPreferencesKey("nisn")
        private val CLASS = stringPreferencesKey("class")
        private val PHONE = stringPreferencesKey("phone")
        private val ADDRESS = stringPreferencesKey("address")
    }

    suspend fun createLoginSession(
        id: Int,
        email: String,
        password: String,
        token: String,
        name: String,
        role: String,
        nisn: String?,
        kelas: String?,
        phone: String?,
        address: String?
    ) {
        dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = true
            prefs[KEY_USER_ID] = id
            prefs[KEY_NAME] = name
            prefs[KEY_EMAIL] = email
            prefs[KEY_TOKEN] = token
            prefs[KEY_ROLE] = role
            prefs[KEY_PASSWORD] = password
            prefs[NISN] = nisn ?: ""
            prefs[CLASS] = kelas ?: ""
            prefs[PHONE] = phone ?: ""
            prefs[ADDRESS] = address ?: ""
        }
        println("DEBUG PREFS: Saving NISN: $nisn, Kelas: $kelas, Phone: $phone")
    }

    // untuk ngmbil token
    val getAuthToken: Flow<String?> = dataStore.data.map { prefs ->
        prefs[KEY_TOKEN]
    }

    // logout
    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }

    val getUserName: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_NAME] ?: "User"
    }

    val getUserEmail: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_EMAIL] ?: "User"
    }

    val getClass: Flow<String> = dataStore.data.map { prefs ->
        prefs[CLASS] ?: "User"
    }

    val getNis: Flow<String> = dataStore.data.map { prefs ->
        prefs[NISN] ?: "User"
    }

    val getUserPhone: Flow<String> = dataStore.data.map { prefs ->
        prefs[PHONE] ?: "User"
    }

    val getUserRole: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_ROLE] ?: "User"
    }

    val getUserAddress: Flow<String> = dataStore.data.map { prefs ->
        prefs[ADDRESS] ?: "User"
    }

    suspend fun isLoggedIn(): Boolean {
        return dataStore.data.map { it[IS_LOGGED_IN] ?: false }.first()
    }

    suspend fun saveAbsenStatus(isAbsen: Boolean, date: String) {
        dataStore.edit { prefs ->
            prefs[IS_ALREADY_ABSEN] = isAbsen
            prefs[LAST_ABSEN_DATE] = date
        }
    }

    val getAbsenStatus: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[IS_ALREADY_ABSEN] ?: false
    }

    val getLastAbsenDate: Flow<String?> = dataStore.data.map { prefs ->
        prefs[LAST_ABSEN_DATE]
    }

    suspend fun getUserData(): UserDetails? {
        val email = getUserEmail.first()
        val id = dataStore.data.map { it[KEY_USER_ID] ?: 0 }.first()

        if (email == "User" || email.isBlank()) return null

        return UserDetails(
            id = id,
            email = email,
            name = getUserName.first(),
            role = getUserRole.first(),
            isAlreadyAbsen = getAbsenStatus.first(),
            phone = getUserPhone.first(),
            nisn = getNis.first(),
            kelas = getClass.first(),
            address = getUserAddress.first()
        )
    }

    suspend fun saveUserName(name: String) {
        dataStore.edit { it[KEY_NAME] = name }
    }

    suspend fun saveUserPhone(phone: String) {
        dataStore.edit { it[PHONE] = phone }
    }

    suspend fun saveClass(kelas: String) {
        dataStore.edit { it[CLASS] = kelas }
    }

    suspend fun saveAddress(address: String) {
        dataStore.edit { it[ADDRESS] = address }
    }

    suspend fun saveNisn(nis: String) {
        dataStore.edit { it[NISN] = nis }
    }
}
