package com.dk.barcocktails.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.barcocktails.domain.login.model.User
import com.dk.barcocktails.domain.login.usecase.SignInUseCase
import com.dk.barcocktails.domain.state.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signInUseCase: SignInUseCase,

    ) : ViewModel() {


    private val _signInState: MutableLiveData<LoadingState<User>> = MutableLiveData()
    val signInState: LiveData<LoadingState<User>> get() = _signInState


    fun signInRequest(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            signInUseCase.invoke(email, password).collect {
                _signInState.postValue(it)
            }

        }
    }
}