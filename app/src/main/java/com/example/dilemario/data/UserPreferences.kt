package com.example.dilemario.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión DataStore
val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val TOKEN_KEY = stringPreferencesKey("auth_token")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_AGE = stringPreferencesKey("user_age")
        val USER_COUNTRY = stringPreferencesKey("user_country")

        // NUEVO → para recordar credenciales
        val LOGIN_EMAIL = stringPreferencesKey("login_email")
        val LOGIN_PASSWORD = stringPreferencesKey("login_password")
    }

    // Guardar TODOS los datos del usuario
    suspend fun saveUserData(
        token: String,
        nombre: String,
        email: String,
        edad: String,
        pais: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[USER_NAME] = nombre
            prefs[USER_EMAIL] = email
            prefs[USER_AGE] = edad
            prefs[USER_COUNTRY] = pais
        }
    }

    // Guardar las últimas credenciales usadas
    suspend fun saveLoginCredentials(email: String, password: String) {
        context.dataStore.edit { prefs ->
            prefs[LOGIN_EMAIL] = email
            prefs[LOGIN_PASSWORD] = password
        }
    }

    // Flujos para leer datos del usuario
    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
    val userName: Flow<String?> = context.dataStore.data.map { it[USER_NAME] }
    val userEmail: Flow<String?> = context.dataStore.data.map { it[USER_EMAIL] }
    val userAge: Flow<String?> = context.dataStore.data.map { it[USER_AGE] }
    val userCountry: Flow<String?> = context.dataStore.data.map { it[USER_COUNTRY] }

    // Flujos para leer credenciales guardadas
    val loginEmail: Flow<String?> = context.dataStore.data.map { it[LOGIN_EMAIL] }
    val loginPassword: Flow<String?> = context.dataStore.data.map { it[LOGIN_PASSWORD] }
}
