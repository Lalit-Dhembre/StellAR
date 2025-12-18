package com.cosmic_struck.stellar.scanTextFeature.presentation

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.stellar.scantext.domain.ImageUploadUseCase
import com.cosmic_struck.stellar.stellar.scantext.presentation.scanScreen.ScanImageScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ScanTextViewModel @Inject constructor(
    private val imageUploadUseCase: ImageUploadUseCase,
    private val application: Application
) : ViewModel() {

    private val _state = mutableStateOf(ScanImageScreenState())
    val state : State<ScanImageScreenState> = _state

    fun captureImage(
        context: Context,
        imageCapture: androidx.camera.core.ImageCapture,
        onImageCaptured: (File) -> Unit
    ) {
        val photoFile = File(
            context.cacheDir,
            "scan_${System.currentTimeMillis()}.jpg"
        )

        val outputOptions =
            androidx.camera.core.ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : androidx.camera.core.ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(
                    outputFileResults: androidx.camera.core.ImageCapture.OutputFileResults
                ) {
                    onImageCaptured(photoFile)
                }

                override fun onError(exception: androidx.camera.core.ImageCaptureException) {
                    Log.e("CameraX", "Capture failed", exception)
                }
            }
        )
    }
    fun resetState() {
        _state.value = ScanImageScreenState()
    }


    fun uploadImage(file: File){
        val requestBody =
            file.asRequestBody("image/jpeg".toMediaTypeOrNull())

        val multipart =
            MultipartBody.Part.createFormData(
                "file",  // must match backend key
                file.name,
                requestBody
            )
        Log.d("VIEWMODEL",multipart.toString())
        viewModelScope.launch {
            imageUploadUseCase.invoke(multipart).collect{ result ->
                when(result){
                    is Resource.Loading<*> -> {
                        _state.value = state.value.copy(
                            isLoading = true
                        )
                        Log.d("VIEWMODEL","LOADING")
                    }
                    is Resource.Error<*> -> {
                        _state.value = state.value.copy(
                        isError = result.message ?: "Unknown error occurred"
                        )
                        Log.d("VIEWMODEL &&",state.value.isError)
                    }
                    is Resource.Success<*> -> {
                        _state.value = state.value.copy(
                            isLoading = false,
                            detections = result.data?.detections ?: emptyList(),
                            count = result.data?.count ?: 0,
                            switchToResults = true
                        )
                        Log.d("VIEWMODEL",state.value.detections.toString())
                    }
                }
            }
        }
    }

}