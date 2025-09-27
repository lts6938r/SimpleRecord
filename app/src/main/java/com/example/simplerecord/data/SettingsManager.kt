package com.example.simplerecord.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

class SettingsManager(private val context: Context) {

    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode_enabled")

    // 一个字符串用于用户昵称
    private val USER_NICKNAME_KEY = stringPreferencesKey("user_nickname")

    // 读取深色模式设置的 Flow
    val isDarkModeEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            // 从 DataStore 中获取值，如果不存在则使用默认值 false
            preferences[DARK_MODE_KEY] ?: false
        }

    // 读取用户昵称设置的 Flow
    val userNickname: Flow<String> = context.dataStore.data
        .map { preferences ->
            // 从 DataStore 中获取值，如果不存在则使用默认值 "匿名用户"
            preferences[USER_NICKNAME_KEY] ?: "匿名用户"
        }

    // 更新深色模式设置的方法
    suspend fun setDarkModeEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }

    // 更新用户昵称的方法
    suspend fun setUserNickname(nickname: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NICKNAME_KEY] = nickname
        }
    }
}