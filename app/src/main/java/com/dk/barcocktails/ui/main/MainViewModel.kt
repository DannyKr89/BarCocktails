package com.dk.barcocktails.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.barcocktails.domain.cocktails.model.Cocktail
import com.dk.barcocktails.domain.login.model.User
import com.dk.barcocktails.domain.profile.usecase.CheckOrganizationUseCase
import com.dk.barcocktails.domain.profile.usecase.CheckPasswordUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val _liveDataCheckOrganization: MutableLiveData<Boolean> = MutableLiveData(),
    private val checkOrganizationUseCase: CheckOrganizationUseCase,
    private val _liveDataCheckPassword: MutableLiveData<Boolean> = MutableLiveData(),
    private val checkPasswordUseCase: CheckPasswordUseCase,
    private val _liveDataCocktail: MutableLiveData<Cocktail> = MutableLiveData(),
    private val _liveDataUser: MutableLiveData<User> = MutableLiveData(),
) : ViewModel() {

    val liveDataCheckOrganization: LiveData<Boolean> get() = _liveDataCheckOrganization
    val liveDataCheckPassword: LiveData<Boolean> get() = _liveDataCheckPassword
    val liveDataCocktail: LiveData<Cocktail> get() = _liveDataCocktail
    val liveDataUser: LiveData<User> get() = _liveDataUser

    private var _pass = ""
    val pass get() = _pass

    fun checkPassword(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            checkPasswordUseCase.invoke(password).collect {
                _pass = password
                _liveDataCheckPassword.postValue(it)
            }
        }
    }

    fun checkOrganization() {
        viewModelScope.launch(Dispatchers.IO) {
            checkOrganizationUseCase.invoke().collect {
                _liveDataCheckOrganization.postValue(it)
            }
        }
    }

    fun user(user: User){
        _liveDataUser.postValue(user)
    }
    fun cocktail(cocktail: Cocktail) {
        _liveDataCocktail.postValue(cocktail)
    }

}