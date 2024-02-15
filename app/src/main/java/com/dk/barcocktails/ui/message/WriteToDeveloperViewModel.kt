package com.dk.barcocktails.ui.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.barcocktails.domain.message.SendMessageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WriteToDeveloperViewModel(
    private val _liveData: MutableLiveData<Boolean> = MutableLiveData(),
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    val liveData: LiveData<Boolean> get() = _liveData


    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            sendMessageUseCase.invoke(message).collect {
                _liveData.postValue(it)
            }
        }
    }
}