package org.guivicj.support.di

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.guivicj.support.domain.repository.SettingsRepository
import org.guivicj.support.domain.usecase.ValidateEmail
import org.guivicj.support.domain.usecase.ValidateName
import org.guivicj.support.domain.usecase.ValidatePassword
import org.guivicj.support.domain.usecase.ValidatePhone
import org.guivicj.support.presentation.ForgotPasswordViewModel
import org.guivicj.support.presentation.LoginViewModel
import org.guivicj.support.presentation.RegisterViewModel
import org.guivicj.support.presentation.SettingsViewModel
import org.guivicj.support.presentation.TechAnalyticsViewModel
import org.guivicj.support.presentation.TechViewModel
import org.guivicj.support.presentation.TicketViewModel
import org.guivicj.support.presentation.UserViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule = module {
    factory { LoginViewModel(get(), get(), get()) }
    factory { RegisterViewModel(get(), get(), get(), get(), get()) }
    single { TicketViewModel(get(), get(), get()) }
    single { UserViewModel(get()) }
    single { ForgotPasswordViewModel(get()) }
    single { TechAnalyticsViewModel(get()) }
    single { SettingsViewModel(get()) }
    single { TechViewModel(get(), get()) }
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

val settingsModule = module {
    single<Settings> { Settings() }
    single { SettingsRepository(get()) }
}

val validationModule = module {
    factory { ValidateEmail() }
    factory { ValidatePassword() }
    factory { ValidateName() }
    factory { ValidatePhone() }
}

fun initializeKoin(vararg modules: Module) {
    startKoin {
        modules(listOf(appModule, networkModule, validationModule, settingsModule) + modules)
    }
}