package com.cosmic_struck.stellar.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(): ViewModel() {

    private val _state = mutableStateOf(HomeScreenState())
    val state: State<HomeScreenState> = _state

    fun onToggle(ind: Int){
        if(ind == 0){
            _state.value = _state.value.copy(selected = Options.MODULES)
        }
        else{
            _state.value = _state.value.copy(selected = Options.CLASSROOM)
        }
    }

}