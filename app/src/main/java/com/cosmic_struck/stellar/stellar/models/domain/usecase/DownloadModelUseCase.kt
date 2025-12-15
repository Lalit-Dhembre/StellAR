package com.cosmic_struck.stellar.modelScreen.domain.usecase

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.cosmic_struck.stellar.common.util.DownloadCompleteReceiver
import com.cosmic_struck.stellar.common.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class DownloadModelUseCase @Inject constructor(
    val context: Application,
    val downloadManager: DownloadManager
) {

    operator fun invoke(url: String, title: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())

        try {
            // Check network connectivity first
            if (!isNetworkAvailable()) {
                emit(Resource.Error("No network connection available"))
                return@flow
            }

            Log.d("DownloadModelUseCase", "Network available, starting download")

            // FIX: Use public Downloads directory instead of app-specific external files.
            // DownloadManager restricts writing to /Android/data/... paths via setDestinationUri
            // This stores files in the public /Download/StellarModels/ folder
            val publicDownloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val appModelDir = File(publicDownloadDir, "StellarModels")

            if (!appModelDir.exists()) {
                appModelDir.mkdirs()
            }

            val targetFile = File(appModelDir, "$title.glb")

            if (targetFile.exists() && targetFile.length() > 0) {
                Log.d("DownloadModelUseCase", "Model already cached: ${targetFile.absolutePath}")
                emit(Resource.Success(targetFile.absolutePath))
                return@flow
            }

            Log.d("DownloadModelUseCase", "Download destination: ${targetFile.absolutePath}")

            // Download to the public Downloads/StellarModels directory
            val req = DownloadManager.Request(url.toUri())
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(title)
                .setDescription("Downloading 3D Model")
                // This is the key fix: Use setDestinationInExternalPublicDir
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "StellarModels/$title.glb")
                .setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI or
                            DownloadManager.Request.NETWORK_MOBILE
                )
                .setAllowedOverRoaming(true)

            Log.d("DownloadModelUseCase", "Enqueueing download for: $title")
            val downloadId = downloadManager.enqueue(req)

            Log.d("DownloadModelUseCase", "Download queued with ID: $downloadId")

            // Set the download ID and file path for the broadcast receiver to use
            DownloadCompleteReceiver.setDownloadInfo(downloadId, targetFile.absolutePath)

            // Collect results from the broadcast receiver
            DownloadCompleteReceiver.getDownloadResult().collect { result ->
                emit(result)
            }

        } catch (e: Exception) {
            Log.e("DownloadModelUseCase", "Download initiation failed: ${e.message}", e)
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    private fun isNetworkAvailable(): Boolean {
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.d("DownloadModelUseCase", "Connected via WiFi")
                    true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.d("DownloadModelUseCase", "Connected via Cellular")
                    true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.d("DownloadModelUseCase", "Connected via Ethernet")
                    true
                }
                else -> false
            }
        } catch (e: Exception) {
            Log.e("DownloadModelUseCase", "Error checking network: ${e.message}")
            return false
        }
    }
}