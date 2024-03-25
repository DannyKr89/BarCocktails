package com.dk.barcocktails.ui.newcocktail

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.barcocktails.domain.cocktails.model.Cocktail
import com.dk.barcocktails.domain.cocktails.usecase.AddCocktailUseCase
import com.dk.barcocktails.domain.image.usecase.LoadImageUseCase
import com.dk.barcocktails.domain.state.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewCocktailViewModel(
    private val loadImageUseCase: LoadImageUseCase,
    private val addCocktailUseCase: AddCocktailUseCase
) : ViewModel() {

    private val _loadImageLiveData: MutableLiveData<LoadingState<String>> = MutableLiveData()
    val loadImageLiveData: LiveData<LoadingState<String>> get() = _loadImageLiveData

    private val _addCocktailLiveData: MutableLiveData<LoadingState<String>> = MutableLiveData()
    val addCocktailLiveData: LiveData<LoadingState<String>> get() = _addCocktailLiveData

    fun loadImage(bitmap: Bitmap, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadImageUseCase.invoke(bitmap, name).collect() {
                _loadImageLiveData.postValue(it)
            }
        }
    }

    fun addCocktail(
        name: String,
        image: String,
        ingredients: LinkedHashMap<String, Int>,
        method: String,
        garnier: String,
        description: String
    ) {
        val cocktail = Cocktail(
            0,
            name, image, ingredients, method, garnier, description
        )
        viewModelScope.launch(Dispatchers.IO) {
            addCocktailUseCase.invoke(cocktail).collect {
                _addCocktailLiveData.postValue(it)
            }
        }
    }

}