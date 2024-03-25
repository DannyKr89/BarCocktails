package com.dk.barcocktails.ui.cocktails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.barcocktails.domain.cocktails.model.Cocktail
import com.dk.barcocktails.domain.cocktails.usecase.DeleteCocktailUseCase
import com.dk.barcocktails.domain.cocktails.usecase.GetCocktailsUseCase
import com.dk.barcocktails.domain.state.LoadingState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CocktailsViewModel(
    private val getCocktailsUseCase: GetCocktailsUseCase,
    private val deleteCocktailUseCase: DeleteCocktailUseCase
) : ViewModel() {


    private val _liveData: MutableLiveData<LoadingState<List<Cocktail>>> = MutableLiveData()
    val liveData: LiveData<LoadingState<List<Cocktail>>> get() = _liveData


    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        _liveData.postValue(LoadingState.Error(throwable))
    }

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