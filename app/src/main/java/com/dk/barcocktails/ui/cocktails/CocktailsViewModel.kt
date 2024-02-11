package com.dk.barcocktails.ui.cocktails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.barcocktails.domain.cocktails.Cocktail
import com.dk.barcocktails.domain.cocktails.DeleteCocktailUseCase
import com.dk.barcocktails.domain.cocktails.GetCocktailsUseCase
import com.dk.barcocktails.domain.cocktails.LoadingState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CocktailsViewModel(
    private val getCocktailsUseCase: GetCocktailsUseCase,
    private val deleteCocktailUseCase: DeleteCocktailUseCase,
    private val _liveData: MutableLiveData<LoadingState<List<Cocktail>>> = MutableLiveData()
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        _liveData.postValue(LoadingState.Error(throwable))
    }

    val liveData: LiveData<LoadingState<List<Cocktail>>> get() = _liveData

    fun getCocktails() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            getCocktailsUseCase.invoke().collect {
                _liveData.postValue(it)
            }
        }
    }

    fun deleteCocktail(cocktail: Cocktail) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteCocktailUseCase.invoke(cocktail)
        }
    }
}