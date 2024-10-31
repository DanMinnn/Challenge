package com.example.currencyconverter.network.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("DEFAULT")

val DEFAULT = stringPreferencesKey("route")


class PreferenceDataUtils @Inject constructor(@ApplicationContext context: Context) {


    private val dataStore = context.dataStore

    suspend fun setDefault(default: String) {
        dataStore.edit { user ->
            user[DEFAULT] = default
        }
    }

    fun getDefault() = dataStore.data.map { default ->
        default[DEFAULT] ?: ""
    }
}