package com.dk.barcocktails.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.barcocktails.domain.profile.usecase.CheckOrganizationUseCase
import com.dk.barcocktails.domain.profile.usecase.CheckPasswordUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val _liveDataCheckOrganization: MutableLiveData<Boolean> = MutableLiveData(),
    private val checkOrganizationUseCase: CheckOrganizationUseCase,
    private val _liveDataCheckPassword: MutableLiveData<Boolean> = MutableLiveData(),
    private val checkPasswordUseCase: CheckPasswordUseCase,
) : ViewModel() {

    val liveDataCheckOrganization: LiveData<Boolean> get() = _liveDataCheckOrganization

    val liveDataCheckPassword: LiveData<Boolean> get() = _liveDataCheckPassword

    fun checkOrganization() {
        viewModelScope.launch(Dispatchers.IO) {
            checkOrganizationUseCase.invoke().collect {
                _liveDataCheckOrganization.postValue(it)
            }
        }
    }

    fun checkPassword(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            checkPasswordUseCase.invoke(password).collect {
                _liveDataCheckPassword.postValue(it)
            }
        }
    }

}