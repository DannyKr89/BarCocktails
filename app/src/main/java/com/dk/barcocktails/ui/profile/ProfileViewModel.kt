package com.dk.barcocktails.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.barcocktails.domain.login.usecase.SignOutUseCase
import com.dk.barcocktails.domain.profile.usecase.LoadProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val signOutUseCase: SignOutUseCase,
    private val _liveDataLoadProfile: MutableLiveData<String> = MutableLiveData(),
    private val loadProfileUseCase: LoadProfileUseCase
) : ViewModel() {
    val liveDataLoadProfile: LiveData<String> get() = _liveDataLoadProfile

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            loadProfileUseCase.invoke().collect {
                _liveDataLoadProfile.postValue(it)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase.invoke()
        }
    }
}