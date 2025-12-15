package com.cosmic_struck.stellar.modelScreen.modelViewerFeature.presentation

import io.github.sceneview.node.ModelNode

data class ModelViewScreenState(
    val rotationSpeed : Float = 0.5f,
    val zoomScale : Float = 0.5f,
    val cameraDistance : Float = 3.0f,
    val modelNode : ModelNode? = null,
    val rotationAngle: Float = 0f,
    val planetName : String? = "Planet",
    val isLoading : Boolean = false,
    val error : String = "",
    val modelURL : String = "",
    val scene : SceneType = SceneType.SceneView
)

enum class SceneType{
    ARSceneView,
    SceneView
}