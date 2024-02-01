package com.dk.barcocktails.di

import com.dk.barcocktails.data.login.FirebaseAuthRepositoryImpl
import com.dk.barcocktails.domain.login.LoginRepository
import com.dk.barcocktails.domain.login.SignInUseCase
import com.dk.barcocktails.domain.login.SignOutUseCase
import com.dk.barcocktails.domain.login.SignUpUseCase
import com.dk.barcocktails.ui.signinsignup.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
}

val loginModule = module {
    single { SignInUseCase(get()) }
    single { SignOutUseCase(get()) }
    single { SignUpUseCase(get()) }
    single<LoginRepository> { FirebaseAuthRepositoryImpl(get()) }
    viewModel { LoginViewModel(signInUseCase = get(), signUpUseCase = get()) }
}