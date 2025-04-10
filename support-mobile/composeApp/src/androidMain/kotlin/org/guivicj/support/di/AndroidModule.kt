package org.guivicj.support.di

import org.guivicj.support.data.repository.AuthRepositoryImpl
import org.guivicj.support.domain.repository.AuthRepository
import org.koin.dsl.module

val androidModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}
