package com.zaniva.storyappv2.connection

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionManager private constructor(private val dataStore: DataStore<Preferences>){
    private val token = stringPreferencesKey("token_user")
    private val name = stringPreferencesKey("user_name")

    fun get(): Flow<String>{
        return dataStore.data.map { preferences ->
            preferences[token] ?: "empty"
            preferences[name] ?: "Intruder"
        }
    }

    fun getToken(): Flow<String>{
        return dataStore.data.map {
            it[token] ?: "empty"
        }
    }

    fun getName(): Flow<String>{
        return dataStore.data.map {
            it[name] ?: "Guest"
        }
    }

    suspend fun save(t: String, n: String) {
        dataStore.edit { preferences ->
            preferences[token] = t
            preferences[name] = n
        }
    }

    suspend fun logout(){

        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var inst: SessionManager? = null

        fun get(dataStore: DataStore<Preferences>): SessionManager {
            return inst ?: synchronized(this){
                val instance = SessionManager(dataStore)
                inst = instance
                instance
            }
        }
    }

}