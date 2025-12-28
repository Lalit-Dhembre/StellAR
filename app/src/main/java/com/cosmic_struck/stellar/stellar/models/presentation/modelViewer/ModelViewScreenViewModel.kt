package com.cosmic_struck.stellar.stellar.models.presentation.modelViewer

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.stellar.models.domain.usecase.DownloadModelUseCase
import com.cosmic_struck.stellar.stellar.models.domain.usecase.GetModelURLUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ModelViewScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getModelURLUseCase: GetModelURLUseCase,
    private val downloadModelUseCase: DownloadModelUseCase
): ViewModel() {

    private val _state = mutableStateOf(ModelViewScreenState())
    val state: State<ModelViewScreenState> = _state

    val planetName = savedStateHandle.get<String>("name")
    val planetId = savedStateHandle.get<String>("id")

    init {
        viewModelScope.launch {
            Log.d("ModelViewScreenViewModel", "Planet Name: $planetName")

            _state.value = _state.value.copy(
                planetName = planetName ?: "Unknown Planet"
            )

            // Auto-download model if path is available
            getModelURL(planetId?:"")

        }
    }

    fun getModelURL(id: String){
        getModelURLUseCase.invoke(id).onEach { result ->
            when(result){
                is Resource.Loading ->{
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        modelURL = result.data ?: "",
                        isLoading = false
                    )
                    downloadModel(state.value.modelURL)
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        error = result.message ?: "Unexpected Error",
                        isLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
    fun downloadModel(url: String) {
        val title = planetName ?: "model"

        viewModelScope.launch {
            downloadModelUseCase(url = url, title = title).collect { result ->
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
                        val filePath = result.data

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
            }
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
    fun resumeDownload(url: String) {
        val title = planetName ?: "model"

        Log.d("ModelViewScreenViewModel", "Attempting to resume download...")
        downloadModel(url)
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