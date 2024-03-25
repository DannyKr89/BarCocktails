package com.dk.barcocktails.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.barcocktails.domain.login.model.User
import com.dk.barcocktails.domain.login.usecase.SignUpUseCase
import com.dk.barcocktails.domain.state.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _signUpState: MutableLiveData<LoadingState<User>> = MutableLiveData()
    val signUpState: LiveData<LoadingState<User>> get() = _signUpState

    fun signUpRequest(email: String, password: String, name: String, adminPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            signUpUseCase.invoke(email, password, name, adminPassword).collect {
                _signUpState.postValue(it)
            }
        }
    }
}