package com.cosmic_struck.stellar.stellar.models.presentation.modelScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.stellar.models.domain.usecase.GetModelsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModelScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getModelsListUseCase: GetModelsListUseCase
): ViewModel() {
    private var _state = mutableStateOf(ModelScreenState())
    val state : State<ModelScreenState> = _state

    init {
        getPlanetsList()
    }

    private fun getPlanetsList(){
        getModelsListUseCase().onEach {it->
            when(it){
                is Resource.Success -> {
                    _state.value.copy(
                        planetsList = it.data ?: emptyList()
                    )
                }
                is Resource.Error -> {
                    _state.value.copy(
                        error = it.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Loading -> {
                    _state.value.copy(
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
    fun onToggleList(){
        viewModelScope.launch {
           _state.value = _state.value.copy(
                currentIndex = if(_state.value.currentIndex == 0) 1 else 0,
                currentList = if(_state.value.currentList == ListType.MY_COLLECTION) ListType.DISCOVER else ListType.MY_COLLECTION
            )
        }
    }

}