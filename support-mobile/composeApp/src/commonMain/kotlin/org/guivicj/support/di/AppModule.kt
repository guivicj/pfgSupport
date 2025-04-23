package org.guivicj.support.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.guivicj.support.domain.usecase.ValidateEmail
import org.guivicj.support.domain.usecase.ValidateName
import org.guivicj.support.domain.usecase.ValidatePassword
import org.guivicj.support.domain.usecase.ValidatePhone
import org.guivicj.support.ui.screens.home.UserViewModel
import org.guivicj.support.ui.screens.home.components.TicketViewModel
import org.guivicj.support.ui.screens.signin.LoginViewModel
import org.guivicj.support.ui.screens.signin.RegisterViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule = module {
    factory { LoginViewModel(get(), get(), get(), get()) }
    factory { RegisterViewModel(get(), get(), get(), get(), get()) }
    single { TicketViewModel(get(), get()) }
    single { UserViewModel() }
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
    factory { ValidateEmail() }
    factory { ValidatePassword() }
    factory { ValidateName() }
    factory { ValidatePhone() }
}

fun initializeKoin(vararg modules: Module) {
    startKoin {
        modules(listOf(appModule, networkModule, validationModule) + modules)
    }
}