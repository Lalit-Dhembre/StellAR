package com.cosmic_struck.stellar.stellar.scantext.presentation.scanResults

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.stellar.models.domain.model.Planet
import com.cosmic_struck.stellar.stellar.models.domain.usecase.GetModelsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanTextResultsViewModel @Inject constructor(
    private val getPlanetsUseCase: GetModelsListUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel(){

    private val _state = mutableStateOf(ScanResultsState())
    val state : State<ScanResultsState> = _state

    init {
        val detects = savedStateHandle.get<String>("detections")
        _state.value = state.value.copy(
            detections = detects?.split(",") ?: emptyList()
        )
        Log.d("DETECTION LIST",state.value.detections.toString())
        getPlanets()
    }

    fun getPlanets(){
        viewModelScope.launch {
            getPlanetsUseCase.invoke().collect { result ->
                when(result){
                    is Resource.Error-> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isError = result.message.toString()
                        )
                    }
                    is Resource.Success<*> -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            planets = result.data?.filter { it.planet_name in state.value.detections[0] }
                                ?: emptyList()
                        )
                        Log.d("RESULTS VIEWMODEL","${state.value.planets}")
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
}