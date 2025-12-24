package com.cosmic_struck.stellar.stellar.scantext.presentation.scanScreen

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.stellar.scantext.domain.ImageUploadUseCase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ScanTextViewModel @Inject constructor(
    private val imageUploadUseCase: ImageUploadUseCase,
    private val textRecognizer: TextRecognizer,
    private val application: Application
) : ViewModel() {

    private val _state = mutableStateOf(ScanImageScreenState())
    val state : State<ScanImageScreenState> = _state

    fun captureImage(
        context: Context,
        imageCapture: ImageCapture,
        onImageCaptured: (File) -> Unit
    ) {
        val photoFile = File(
            context.cacheDir,
            "scan_${System.currentTimeMillis()}.jpg"
        )

        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(
                    outputFileResults: ImageCapture.OutputFileResults
                ) {
                    onImageCaptured(photoFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraX", "Capture failed", exception)
                }
            }
        )
    }
    fun resetState() {
        _state.value = ScanImageScreenState()
    }


    fun uploadImage(file: File){

        val image = InputImage.fromFilePath(
            application,
            Uri.fromFile(file)
        )

        textRecognizer.process(image)
            .addOnSuccessListener { it->
                val extractedText = it.text
                val count = it.textBlocks.size
                Log.d("EXTRACTED TEXT",extractedText)
            }
            .addOnFailureListener { e ->
                Log.d("FAILURE EXTRACTION",e.localizedMessage ?: "Unknown error occurred")
            }
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