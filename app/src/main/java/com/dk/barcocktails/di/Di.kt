package com.dk.barcocktails.di

import com.dk.barcocktails.data.cocktails.CocktailsRepositoryImpl
import com.dk.barcocktails.data.login.FirebaseAuthRepositoryImpl
import com.dk.barcocktails.domain.cocktails.AddCocktailUseCase
import com.dk.barcocktails.domain.cocktails.CocktailsRepository
import com.dk.barcocktails.domain.cocktails.GetCocktailsUseCase
import com.dk.barcocktails.domain.login.LoginRepository
import com.dk.barcocktails.domain.login.SignInUseCase
import com.dk.barcocktails.domain.login.SignOutUseCase
import com.dk.barcocktails.domain.login.SignUpUseCase
import com.dk.barcocktails.ui.cocktails.CocktailsViewModel
import com.dk.barcocktails.ui.signinsignup.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { Firebase.firestore }
}

val loginModule = module {
    single { SignInUseCase(get()) }
    single { SignOutUseCase(get()) }
    single { SignUpUseCase(get()) }
    single<LoginRepository> { FirebaseAuthRepositoryImpl(get()) }
    viewModel<LoginViewModel> { LoginViewModel(signInUseCase = get(), signUpUseCase = get()) }
}

val cocktailsModule = module {
    single<CocktailsRepository> { CocktailsRepositoryImpl(get(), get()) }
    single<GetCocktailsUseCase> { GetCocktailsUseCase(get()) }
    viewModel<CocktailsViewModel> { CocktailsViewModel(get()) }
}

val newCocktailModule = module {
    single<AddCocktailUseCase> { AddCocktailUseCase(get()) }
}