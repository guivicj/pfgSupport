package org.guivicj.support.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.guivicj.support.ui.screens.signin.LoginViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule = module {
    factory { LoginViewModel(get()) }
}

val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }
}

val validationModule = module {
}

fun initializeKoin(vararg modules: Module) {
    startKoin {
        modules(listOf(appModule, networkModule, validationModule) + modules)
    }
}