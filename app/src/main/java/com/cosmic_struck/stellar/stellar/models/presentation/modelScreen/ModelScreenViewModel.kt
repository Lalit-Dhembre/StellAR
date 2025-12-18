package com.cosmic_struck.stellar.stellar.models.presentation.modelScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.stellar.models.domain.model.Planet
import com.cosmic_struck.stellar.stellar.models.domain.usecase.GetModelsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
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

    private var _collectedList = mutableListOf<Planet>()
    private var _discoverList = mutableListOf<Planet>()

    val collectedList : List<Planet> = _collectedList
    val discoverList : List<Planet> = _discoverList


    init {
         getPlanetsList()
    }


    private fun listSegregator(planets : List<Planet>){
        viewModelScope.launch{
            planets.map { it ->
                if (it.min_level <= state.value.min_level) {
                    _collectedList.add(it)
                } else {
                    _discoverList.add(it)
                }
            }
        }
    }
    private fun getPlanetsList(){
        getModelsListUseCase.invoke().onEach { it->

            when(it){
                is Resource.Error<*> -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = it.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Success<*> -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        planetsList = it.data ?: emptyList()
                    )
                    listSegregator(planets = it.data ?: emptyList())
                }
                is Resource.Loading<*> -> {
                    _state.value = _state.value.copy(
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