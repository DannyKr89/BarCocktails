package com.dk.barcocktails.ui.signinsignup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.barcocktails.domain.login.SignInSignUpState
import com.dk.barcocktails.domain.login.SignInUseCase
import com.dk.barcocktails.domain.login.SignUpUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val _signInState: MutableLiveData<SignInSignUpState> = MutableLiveData(),
    private val _signUpState: MutableLiveData<SignInSignUpState> = MutableLiveData(),
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase

) : ViewModel() {

    val signInState: LiveData<SignInSignUpState> get() = _signInState
    val signUpState: LiveData<SignInSignUpState> get() = _signUpState


    fun signInRequest(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            signInUseCase.signIn(email, password).collect {
                if (it) {
                    _signInState.postValue(SignInSignUpState.Success("Success"))
                } else {
                    _signInState.postValue(SignInSignUpState.Error(Throwable("Error")))
                }
            }

        }
    }

    fun signUpRequest(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            signUpUseCase.signUp(email, password).collect {
                if (it) {
                    _signUpState.postValue(SignInSignUpState.Success("Success"))
                } else {
                    _signUpState.postValue(SignInSignUpState.Error(Throwable("Error")))
                }
            }

        }
    }
}