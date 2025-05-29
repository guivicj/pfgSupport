package org.guivicj.support.di

import org.guivicj.support.data.repository.AuthRepositoryImpl
import org.guivicj.support.data.repository.TechRepositoryImpl
import org.guivicj.support.data.repository.TicketRepositoryImpl
import org.guivicj.support.data.repository.UserRepositoryImpl
import org.guivicj.support.domain.repository.AuthRepository
import org.guivicj.support.domain.repository.TechRepository
import org.guivicj.support.domain.repository.TicketRepository
import org.guivicj.support.domain.repository.UserRepository
import org.guivicj.support.firebase.FirebaseAuthProvider
import org.guivicj.support.firebase.FirebaseTokenProvider
import org.koin.dsl.module

val androidModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<TicketRepository> { TicketRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<FirebaseTokenProvider> { FirebaseAuthProvider() }
    single<TechRepository> { TechRepositoryImpl(get()) }
}
