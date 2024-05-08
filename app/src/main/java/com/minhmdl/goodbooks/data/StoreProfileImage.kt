package com.minhmdl.goodbooks.data

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreProfileImage(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("profileImage")
        val USER_IMAGE_KEY = stringPreferencesKey("profile_image")
    }

    val getImagePath: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_IMAGE_KEY] ?: ""
    }

    suspend fun saveImagePath(uri: Uri) {
        context.dataStore.edit { preferences ->
            preferences[USER_IMAGE_KEY] = uri.toString()
        }
    }
}