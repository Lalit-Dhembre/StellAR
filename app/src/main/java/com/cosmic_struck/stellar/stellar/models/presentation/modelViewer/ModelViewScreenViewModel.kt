package com.cosmic_struck.stellar.modelScreen.modelViewerFeature.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.common.util.earthUrl
import com.cosmic_struck.stellar.modelScreen.domain.usecase.DownloadModelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ModelViewScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val downloadModelUseCase: DownloadModelUseCase
): ViewModel() {

    private val _state = mutableStateOf(ModelViewScreenState())
    val state: State<ModelViewScreenState> = _state

    val planetName = savedStateHandle.get<String>("name")

    init {
        viewModelScope.launch {
            Log.d("ModelViewScreenViewModel", "Planet Name: $planetName")

            _state.value = _state.value.copy(
                planetName = planetName ?: "Unknown Planet"
            )

            // Auto-download model if path is available
           downloadModel()
        }
    }

    fun downloadModel() {
        val url = earthUrl
        val title = planetName ?: "model"

        viewModelScope.launch {
            downloadModelUseCase(url = url, title = title).onEach { result ->
                when(result) {
                    is Resource.Error<*> -> {
                        _state.value = _state.value.copy(
                            error = result.message ?: "Unexpected Error",
                            isLoading = false
                        )
                        Log.e("ModelViewScreenViewModel", "Download Error: ${result.message}")
                    }

                    is Resource.Loading<*> -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            error = ""
                        )
                        Log.d("ModelViewScreenViewModel", "Model download started")
                    }

                    is Resource.Success<*> -> {
                        val filePath = result.data as? String

                        if (filePath != null && File(filePath).exists()) {
                            val fileSize = File(filePath).length()
                            Log.d("ModelViewScreenViewModel", "Download successful. File path: $filePath, Size: $fileSize bytes")

                            _state.value = _state.value.copy(
                                modelURL = filePath,
                                isLoading = false,
                                error = ""
                            )
                        } else {
                            Log.e("ModelViewScreenViewModel", "File not found at: $filePath")
                            _state.value = _state.value.copy(
                                error = "Downloaded file not found at: $filePath",
                                isLoading = false
                            )
                        }
                    }
                }
            }.collect { }
        }
    }

    fun toggleScene() {
        viewModelScope.launch {
            if (_state.value.scene == SceneType.SceneView) {
                _state.value = _state.value.copy(
                    scene = SceneType.ARSceneView
                )
            } else {
                _state.value = _state.value.copy(
                    scene = SceneType.SceneView
                )
            }
        }
    }
    fun resumeDownload() {
        val url = earthUrl
        val title = planetName ?: "model"

        Log.d("ModelViewScreenViewModel", "Attempting to resume download...")
        downloadModel()
    }

    fun onChangeRotation(rotationSpeed: Float) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                rotationSpeed = rotationSpeed.coerceIn(0f, 5f)
            )
        }
    }

    fun onChangeZoomScale(zoomScale: Float) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                zoomScale = zoomScale.coerceIn(0.1f, 3f)
            )
        }
    }

    fun onChangeCameraDistance(cameraDistance: Float) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                cameraDistance = cameraDistance.coerceIn(1f, 10f)
            )
        }
    }

    fun onChangeModelNode(modelNode: ModelNode) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                modelNode = modelNode
            )
        }
    }

    fun onChangeRotationAngle(rotationAngle: Float) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                rotationAngle = rotationAngle
            )
        }
    }

    fun resetModel() {
        _state.value = ModelViewScreenState(
            planetName = planetName
        )
    }
}