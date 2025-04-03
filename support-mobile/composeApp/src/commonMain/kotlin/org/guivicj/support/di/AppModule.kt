package org.guivicj.support.di

import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {

}

val validationModule = module {

}

fun initializeKoin() {
    startKoin {
        modules(appModule, validationModule)
    }
}