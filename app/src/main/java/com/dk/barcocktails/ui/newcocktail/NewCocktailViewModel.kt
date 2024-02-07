package com.dk.barcocktails.ui.newcocktail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.barcocktails.domain.cocktails.AddCocktailUseCase
import com.dk.barcocktails.domain.cocktails.Cocktail
import com.dk.barcocktails.domain.cocktails.LoadingState
import com.dk.barcocktails.domain.image.LoadImageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewCocktailViewModel(
    private val loadImageUseCase: LoadImageUseCase,
    private val addCocktailUseCase: AddCocktailUseCase,
    private val _loadImageLiveData: MutableLiveData<LoadingState<String>> = MutableLiveData(),
    private val _addCocktailLiveData: MutableLiveData<LoadingState<String>> = MutableLiveData(),
) : ViewModel() {

    val loadImageLiveData: LiveData<LoadingState<String>> get() = _loadImageLiveData
    val addCocktailLiveData: LiveData<LoadingState<String>> get() = _addCocktailLiveData
    fun loadImage(uri: String) {
        _loadImageLiveData.postValue(LoadingState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            _loadImageLiveData.postValue(loadImageUseCase.invoke(uri))
        }
    }

    fun addCocktail(
        name: String,
        image: String,
        ingredients: HashMap<String, Int>,
        method: String,
        garnier: String
    ) {
        val cocktail = Cocktail(
            0,
            name, image, ingredients, method, garnier
        )
        _addCocktailLiveData.postValue(LoadingState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            _addCocktailLiveData.postValue(addCocktailUseCase.invoke(cocktail))
        }
    }


}