package com.dk.barcocktails.ui.cocktails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.barcocktails.domain.cocktails.GetCocktailsUseCase
import com.dk.barcocktails.domain.cocktails.LoadingState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CocktailsViewModel(
    private val getCocktailsUseCase: GetCocktailsUseCase,
    private val _liveData: MutableLiveData<LoadingState> = MutableLiveData()
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        _liveData.postValue(LoadingState.Error(throwable))
    }

    val liveData: LiveData<LoadingState> get() = _liveData

    fun getCocktails() {
        _liveData.postValue(LoadingState.Loading)
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _liveData.postValue(LoadingState.Success(getCocktailsUseCase.getCocktails()))
        }
    }
}