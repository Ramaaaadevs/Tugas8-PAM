package com.diwan.myprofileapp.shared.di

import android.content.Context
import com.diwan.myprofileapp.shared.ai.GeminiService
import com.diwan.myprofileapp.shared.data.DatabaseDriverFactory
import com.diwan.myprofileapp.shared.data.SettingsRepository
import com.diwan.myprofileapp.shared.platform.BatteryInfo
import com.diwan.myprofileapp.shared.platform.DeviceInfo
import com.diwan.myprofileapp.shared.platform.NetworkMonitor
import com.russhwolf.settings.SharedPreferencesSettings
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidModule = module {
    single { DatabaseDriverFactory(androidContext()) }
    single {
        val prefs = androidContext()
            .getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        SettingsRepository(SharedPreferencesSettings(prefs))
    }
    single { DeviceInfo() }
    single { NetworkMonitor() }
    single { BatteryInfo() }

    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    single {
        val apiKey = com.diwan.myprofileapp.BuildConfig.GEMINI_API_KEY
        GeminiService(get(), apiKey)
    }
}