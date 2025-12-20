@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.cosmic_struck.stellar.stellar.arlab.presentation

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.unity3d.player.UnityPlayerActivity


@Composable
fun ARLabScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val intent = Intent(context, UnityPlayerActivity::class.java)
    context.startActivity(intent)
}