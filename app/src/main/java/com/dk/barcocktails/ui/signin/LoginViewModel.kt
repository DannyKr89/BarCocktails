package com.dk.barcocktails.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.barcocktails.domain.login.state.SignInSignUpState
import com.dk.barcocktails.domain.login.usecase.SignInUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val _signInState: MutableLiveData<SignInSignUpState> = MutableLiveData(),
    private val signInUseCase: SignInUseCase

) : ViewModel() {

    val signInState: LiveData<SignInSignUpState> get() = _signInState


    fun signInRequest(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            signInUseCase.invoke(email, password).collect {
                if (it) {
                    _signInState.postValue(SignInSignUpState.Success("Success"))
                } else {
                    _signInState.postValue(SignInSignUpState.Error(Throwable("Error")))
                }
            }

        }
    }
}