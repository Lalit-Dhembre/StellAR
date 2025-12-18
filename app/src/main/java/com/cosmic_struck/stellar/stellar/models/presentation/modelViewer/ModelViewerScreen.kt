package com.cosmic_struck.stellar.modelScreen.modelViewerFeature.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.modelScreen.modelViewerFeature.presentation.components.ControlPanel
import com.cosmic_struck.stellar.modelScreen.modelViewerFeature.presentation.components.SceneView
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberView
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelViewerScreen(
    viewModel: ModelViewScreenViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state = viewModel.state.value
                if (state.isLoading) {
                    val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.space_shuttle1))
                    val isPlaying = remember { mutableStateOf(true) }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        LottieAnimation(
                            composition = composition.value,
                            isPlaying = isPlaying.value,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier
                                .fillMaxSize()
                                .navigationBarsPadding()
                                .padding(horizontal = 20.dp, vertical = 20.dp),
                            contentScale = ContentScale.Fit
                        )

                    }

                } else if (state.error.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading model",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.error,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.downloadModel(state.modelURL) }
                        ) {
                            Text("Retry")
                        }
                    }

                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        val engine = rememberEngine()
                        val modelLoader = rememberModelLoader(engine)
                        val materialLoader = rememberMaterialLoader(engine)
                        val environmentLoader = rememberEnvironmentLoader(engine)
                        val view = rememberView(engine)
                        val isValidModelPath = remember(state.modelURL) {
                            if (state.modelURL.isEmpty()) {
                                Log.d("SceneView", "Model path is null or empty")
                                false
                            } else {
                                try {
                                    val file = File(state.modelURL)
                                    val isValid = file.exists() && file.isFile && file.canRead() && file.length() > 0
                                    Log.d("ModelViewerScreen", "Model validation: path=${state.modelURL}, exists=${file.exists()}, isFile=${file.isFile}, canRead=${file.canRead()}, size=${file.length()}, isValid = ${isValid.toString()}")
                                    isValid
                                } catch (e: Exception) {
                                    Log.e("ModelViewerScreen", "Error validating model path: ${e.message}")
                                    false
                                }
                            }
                        }

                        if ( isValidModelPath && state.modelURL.isNotEmpty()) {
                            Log.d("Model Viewer Screen","Model path is valid")
//                            if(state.scene == SceneType.SceneView){
                                SceneView(
                                    modifier = Modifier
                                        .align(Alignment.Center),
                                    cameraDistance = state.cameraDistance,
                                    rotationSpeed = state.rotationSpeed,
                                    zoomScale = state.zoomScale,
                                    rotationAngle = state.rotationAngle,
                                    modelPath = state.modelURL,
                                    onChangeRotationAngle = { viewModel.onChangeRotationAngle(it) },
                                    onChangeModelNode = { viewModel.onChangeModelNode(it) },
                                    engine = engine,
                                    modelLoader = modelLoader,
                                    materialLoader = materialLoader,
                                    environmentLoader = environmentLoader,
                                    view = view,
                                )
//                            }
//                            else{
//                                ARSceneView(
//                                    modifier = Modifier
//                                        .align(Alignment.Center),
//                                    cameraDistance = state.cameraDistance,
//                                    rotationSpeed = state.rotationSpeed,
//                                    zoomScale = state.zoomScale,
//                                    rotationAngle = state.rotationAngle,
//                                    modelPath = state.modelURL,
//                                    onChangeRotationAngle = { viewModel.onChangeRotationAngle(it) },
//                                    onChangeModelNode = { viewModel.onChangeModelNode(it) },
//                                    engine = engine,
//                                    modelLoader = modelLoader,
//                                    materialLoader = materialLoader,
//                                    environmentLoader = environmentLoader,
//                                    view = view,
//                                )
//                            }
                        }

                        ControlPanel(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            rotationSpeed = state.rotationSpeed,
                            zoomScale = state.zoomScale,
                            cameraDistance = state.cameraDistance,
                            onRotationSpeedChange = { viewModel.onChangeRotation(it) },
                            onZoomScaleChange = { viewModel.onChangeZoomScale(it) },
                            onCameraDistanceChange = { viewModel.onChangeCameraDistance(it) },
                            scene = state.scene,
                            onToggleScene = { viewModel.toggleScene() }
                        )

                }
            }
        }
