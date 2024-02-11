package com.dk.barcocktails.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.dk.barcocktails.data.cocktails.CocktailsRepositoryImpl
import com.dk.barcocktails.data.cocktails.image.ImageRepositoryImpl
import com.dk.barcocktails.data.login.FirebaseAuthRepositoryImpl
import com.dk.barcocktails.domain.cocktails.AddCocktailUseCase
import com.dk.barcocktails.domain.cocktails.CocktailsRepository
import com.dk.barcocktails.domain.cocktails.DeleteCocktailUseCase
import com.dk.barcocktails.domain.cocktails.GetCocktailsUseCase
import com.dk.barcocktails.domain.image.ImageRepository
import com.dk.barcocktails.domain.image.LoadImageUseCase
import com.dk.barcocktails.domain.login.LoginRepository
import com.dk.barcocktails.domain.login.SignInUseCase
import com.dk.barcocktails.domain.login.SignOutUseCase
import com.dk.barcocktails.domain.login.SignUpUseCase
import com.dk.barcocktails.ui.cocktails.CocktailsAdapter
import com.dk.barcocktails.ui.cocktails.CocktailsViewModel
import com.dk.barcocktails.ui.newcocktail.NewCocktailViewModel
import com.dk.barcocktails.ui.signinsignup.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { Firebase.firestore }
    single { Firebase.storage }
}

val loginModule = module {
    single { SignInUseCase(loginRepository = get()) }
    single { SignOutUseCase(loginRepository = get()) }
    single { SignUpUseCase(loginRepository = get()) }
    single<LoginRepository> { FirebaseAuthRepositoryImpl(auth = get()) }
    viewModel<LoginViewModel> { LoginViewModel(signInUseCase = get(), signUpUseCase = get()) }
}

val cocktailsModule = module {
    single<ImageLoader> {
        ImageLoader(get()).newBuilder()
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(get())
                    .maxSizePercent(0.25)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.4)
                    .directory(get<Context>().cacheDir)
                    .build()
            }
            .logger(DebugLogger())
            .build()
    }
    single<CocktailsRepository> { CocktailsRepositoryImpl(db = get(), auth = get()) }
    single<GetCocktailsUseCase> { GetCocktailsUseCase(cocktailsRepository = get()) }
    single<DeleteCocktailUseCase> { DeleteCocktailUseCase(cocktailsRepository = get()) }
    single<CocktailsAdapter> { CocktailsAdapter(imgLoader = get()) }
    viewModel<CocktailsViewModel> {
        CocktailsViewModel(
            getCocktailsUseCase = get(),
            deleteCocktailUseCase = get()
        )
    }
}

val newCocktailModule = module {
    single<ImageRepository> { ImageRepositoryImpl(auth = get(), storage = get()) }
    single<LoadImageUseCase> { LoadImageUseCase(imageRepository = get()) }
    single<AddCocktailUseCase> { AddCocktailUseCase(cocktailsRepository = get()) }
    viewModel { NewCocktailViewModel(loadImageUseCase = get(), addCocktailUseCase = get()) }
}