package com.dk.barcocktails

import android.app.Application
import com.dk.barcocktails.di.appModule
import com.dk.barcocktails.di.cocktailsModule
import com.dk.barcocktails.di.loginModule
import com.dk.barcocktails.di.newCocktailModule
import com.dk.barcocktails.di.profileModule
import com.dk.barcocktails.di.signUpModule
import com.dk.barcocktails.di.writeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(
                appModule,
                loginModule,
                signUpModule,
                cocktailsModule,
                newCocktailModule,
                profileModule,
                writeModule
            )
        }
    }
}