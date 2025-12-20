package com.cosmic_struck.stellar.modelScreen.modelViewerFeature.presentation.components

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import coil.request.Disposable
import com.google.android.filament.Engine
import com.google.android.filament.View
import io.github.sceneview.Scene
import io.github.sceneview.collision.HitResult
import io.github.sceneview.loaders.EnvironmentLoader
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import io.github.sceneview.model.Model
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView
import java.io.File
import java.nio.ByteBuffer

@Composable
fun SceneView(
    engine: Engine,
    modelLoader: ModelLoader,
    materialLoader: MaterialLoader,
    environmentLoader: EnvironmentLoader,
    view: View,
    cameraDistance: Float,
    rotationSpeed: Float,
    zoomScale: Float,
    rotationAngle: Float,
    modelPath: String?,
    onChangeRotationAngle: (Float) -> Unit,
    onChangeModelNode: (ModelNode) -> Unit,
    modifier: Modifier = Modifier
) {
    val loadedModelNode = remember { mutableStateOf<ModelNode?>(null) }
    val baseScale = remember { mutableStateOf(1f) }

    Scene(
        modifier = modifier.fillMaxSize(),
        engine = engine,
        view = view,
        renderer = rememberRenderer(engine),
        scene = rememberScene(engine),
        modelLoader = modelLoader,
        materialLoader = materialLoader,
        environmentLoader = environmentLoader,
        collisionSystem = rememberCollisionSystem(view),

        // Light
        mainLightNode = rememberMainLightNode(engine) {
            intensity = 100_000f
        },

        // Camera
        cameraNode = rememberCameraNode(engine) {
            position = Position(0f, 0f, cameraDistance)
        },

        cameraManipulator = rememberCameraManipulator(),

        // Load model
        childNodes = rememberNodes {
            try {
                val file = File(modelPath ?: return@rememberNodes)

                if (!file.exists() || !file.canRead()) {
                    Log.e("SceneView", "Invalid model file: $modelPath")
                    return@rememberNodes
                }

                val buffer = file.inputStream().use {
                    ByteBuffer.wrap(it.readBytes())
                }

                val modelInstance = modelLoader.createModelInstance(buffer)

                val node = ModelNode(modelInstance).apply {

                    // ---- NORMALIZE MODEL SIZE (ONCE) ----
                    val bounds = boundingBox

                    val sizeX = bounds.halfExtent.size * 2f
                    val sizeY = bounds.halfExtent.size * 2f
                    val sizeZ = bounds.halfExtent.size * 2f

                    val maxDim = maxOf(sizeX, sizeY, sizeZ)
                    val normalizedScale = if (maxDim > 0f) 1f / maxDim else 1f

                    baseScale.value = normalizedScale

                    scale = Scale(
                        x = normalizedScale,
                        y = normalizedScale,
                        z = normalizedScale
                    )

                    position = Position(0f, 0f, 0f)
                    rotation = Rotation(y = rotationAngle)
                }

                loadedModelNode.value = node
                onChangeModelNode(node)
                add(node)

                Log.d("SceneView", "Model loaded & normalized")

            } catch (e: Exception) {
                Log.e("SceneView", "Model load error", e)
            }
        },

        // Gestures
        onGestureListener = rememberOnGestureListener(
            onDoubleTapEvent = { _, tappedNode ->
                tappedNode?.let {
                    val current = it.scale.x
                    val newScale = (current * 1.2f).coerceIn(
                        baseScale.value * 0.2f,
                        baseScale.value * 5f
                    )

                    it.scale = Scale(newScale, newScale, newScale)
                }
            }
        ),

        onTouchEvent = { _: MotionEvent, _: HitResult? -> false },

        // Rotation only (continuous)
        onFrame = {
            loadedModelNode.value?.let { node ->
                val newAngle = rotationAngle + rotationSpeed * 0.016f
                onChangeRotationAngle(newAngle)
                node.rotation = Rotation(y = newAngle)
            }
        }
    )

    // ---- APPLY ZOOM SCALE (ONLY WHEN IT CHANGES) ----
    LaunchedEffect(zoomScale) {
        loadedModelNode.value?.let { node ->
            val finalScale = baseScale.value * zoomScale
            node.scale = Scale(finalScale, finalScale, finalScale)
        }
    }

}
