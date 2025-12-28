package com.cosmic_struck.stellar.classroom.presentation.viewmodel.delegate

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.cosmic_struck.stellar.classroom.presentation.viewmodel.ClassroomModelScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ClassroomModelScreenDelegate @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    ) {
    private val _state = MutableStateFlow(ClassroomModelScreenState())
    val state: StateFlow<ClassroomModelScreenState> = _state

    init {
        val modelUrl = savedStateHandle.get<String>("model_url")
        val modelName = savedStateHandle.get<String>("title")
        if(modelUrl != null && modelName != null){
            _state.value = _state.value.copy(
                modelURL = modelUrl,
                modelName = modelName
            )
            Log.d("Model URL",state.value.modelURL)
        }
    }

    suspend fun downloadModel(){
    }
}