package com.dk.barcocktails.ui.newcocktail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.barcocktails.domain.cocktails.model.Cocktail
import com.dk.barcocktails.domain.cocktails.state.LoadingState
import com.dk.barcocktails.domain.cocktails.usecase.AddCocktailUseCase
import com.dk.barcocktails.domain.image.usecase.LoadImageUseCase
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
        viewModelScope.launch(Dispatchers.IO) {
            loadImageUseCase.invoke(uri).collect() {
                _loadImageLiveData.postValue(it)
            }
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
        viewModelScope.launch(Dispatchers.IO) {
            addCocktailUseCase.invoke(cocktail).collect {
                _addCocktailLiveData.postValue(it)
            }
        }
    }


}