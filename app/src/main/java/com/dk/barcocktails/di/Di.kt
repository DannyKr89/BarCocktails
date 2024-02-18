package com.dk.barcocktails.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.dk.barcocktails.data.cocktails.CocktailsRepositoryImpl
import com.dk.barcocktails.data.image.ImageRepositoryImpl
import com.dk.barcocktails.data.login.FirebaseAuthRepositoryImpl
import com.dk.barcocktails.data.message.WriteToDeveloperRepositoryImpl
import com.dk.barcocktails.data.profile.ProfileRepositoryImpl
import com.dk.barcocktails.domain.cocktails.repository.CocktailsRepository
import com.dk.barcocktails.domain.cocktails.usecase.AddCocktailUseCase
import com.dk.barcocktails.domain.cocktails.usecase.DeleteCocktailUseCase
import com.dk.barcocktails.domain.cocktails.usecase.GetCocktailsUseCase
import com.dk.barcocktails.domain.image.repository.ImageRepository
import com.dk.barcocktails.domain.image.usecase.LoadImageUseCase
import com.dk.barcocktails.domain.login.repository.LoginRepository
import com.dk.barcocktails.domain.login.usecase.SignInUseCase
import com.dk.barcocktails.domain.login.usecase.SignOutUseCase
import com.dk.barcocktails.domain.login.usecase.SignUpUseCase
import com.dk.barcocktails.domain.message.repository.WriteToDeveloperRepository
import com.dk.barcocktails.domain.message.usecase.SendMessageUseCase
import com.dk.barcocktails.domain.profile.repository.ProfileRepository
import com.dk.barcocktails.domain.profile.usecase.CheckOrganizationUseCase
import com.dk.barcocktails.domain.profile.usecase.CheckPasswordUseCase
import com.dk.barcocktails.domain.profile.usecase.LoadProfileUseCase
import com.dk.barcocktails.ui.cocktails.CocktailsAdapter
import com.dk.barcocktails.ui.cocktails.CocktailsViewModel
import com.dk.barcocktails.ui.main.MainViewModel
import com.dk.barcocktails.ui.message.WriteToDeveloperViewModel
import com.dk.barcocktails.ui.newcocktail.NewCocktailViewModel
import com.dk.barcocktails.ui.profile.ProfileViewModel
import com.dk.barcocktails.ui.signin.LoginViewModel
import com.dk.barcocktails.ui.signup.SignUpViewModel
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
    single<ProfileRepository> { ProfileRepositoryImpl(db = get(), auth = get()) }
    single<LoginRepository> { FirebaseAuthRepositoryImpl(auth = get(), db = get()) }
    single<CheckOrganizationUseCase> { CheckOrganizationUseCase(get()) }
    single<CheckPasswordUseCase> { CheckPasswordUseCase(get()) }
    viewModel<MainViewModel> {
        MainViewModel(
            checkOrganizationUseCase = get(), checkPasswordUseCase = get()
        )
    }
}

val loginModule = module {
    single { SignInUseCase(loginRepository = get()) }
    viewModel<LoginViewModel> { LoginViewModel(signInUseCase = get()) }
}

val signUpModule = module {
    single { SignUpUseCase(loginRepository = get()) }
    viewModel<SignUpViewModel> { SignUpViewModel(signUpUseCase = get()) }
}

val cocktailsModule = module {
    single<ImageLoader> {
        ImageLoader(get()).newBuilder().memoryCachePolicy(CachePolicy.ENABLED).memoryCache {
            MemoryCache.Builder(get()).maxSizePercent(0.25).strongReferencesEnabled(true)
                .build()
        }.diskCachePolicy(CachePolicy.ENABLED).diskCache {
            DiskCache.Builder().maxSizePercent(0.4).directory(get<Context>().cacheDir).build()
        }.logger(DebugLogger()).build()
    }
    single<CocktailsRepository> { CocktailsRepositoryImpl(db = get(), auth = get()) }
    single<GetCocktailsUseCase> { GetCocktailsUseCase(cocktailsRepository = get()) }
    single<DeleteCocktailUseCase> { DeleteCocktailUseCase(cocktailsRepository = get()) }
    single<CocktailsAdapter> { CocktailsAdapter(imgLoader = get()) }
    viewModel<CocktailsViewModel> {
        CocktailsViewModel(
            getCocktailsUseCase = get(), deleteCocktailUseCase = get()
        )
    }
}

val newCocktailModule = module {
    single<ImageRepository> { ImageRepositoryImpl(auth = get(), storage = get()) }
    single<LoadImageUseCase> { LoadImageUseCase(imageRepository = get()) }
    single { SignOutUseCase(loginRepository = get()) }
    single<AddCocktailUseCase> { AddCocktailUseCase(cocktailsRepository = get()) }
    viewModel<NewCocktailViewModel> {
        NewCocktailViewModel(
            loadImageUseCase = get(),
            addCocktailUseCase = get()
        )
    }
}

val profileModule = module {
    single<LoadProfileUseCase> { LoadProfileUseCase(profileRepository = get()) }
    viewModel<ProfileViewModel> {
        ProfileViewModel(
            signOutUseCase = get(), loadProfileUseCase = get()
        )
    }
}
val writeModule = module {
    single<WriteToDeveloperRepository> { WriteToDeveloperRepositoryImpl(auth = get(), db = get()) }
    single<SendMessageUseCase> { SendMessageUseCase(repository = get()) }
    viewModel<WriteToDeveloperViewModel> { WriteToDeveloperViewModel(sendMessageUseCase = get()) }
}