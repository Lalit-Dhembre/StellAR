package com.cosmic_struck.stellar.stellar.models.domain.usecase

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.cosmic_struck.stellar.common.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class DownloadModelUseCase @Inject constructor(
    val context: Application,
    val httpClient: OkHttpClient  // Add OkHttp to your dependencies
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

            // Create app's private cache directory (no permissions needed, always available)
            val appPrivateDir = File(context.cacheDir, "StellarModels")
            if (!appPrivateDir.exists()) {
                appPrivateDir.mkdirs()
            }

            val targetFile = File(appPrivateDir, "$title.glb")

            // Check if file already exists
            if (targetFile.exists() && targetFile.length() > 0) {
                Log.d("DownloadModelUseCase", "Model already cached: ${targetFile.absolutePath}")
                emit(Resource.Success(targetFile.absolutePath))
                return@flow
            }

            Log.d("DownloadModelUseCase", "Download destination: ${targetFile.absolutePath}")

            // Use OkHttp for direct download to private storage
            val request = Request.Builder()
                .url(url)
                .build()

            val response = httpClient.newCall(request).execute()

            if (!response.isSuccessful) {
                emit(Resource.Error("Download failed with status: ${response.code}"))
                return@flow
            }

            val body = response.body
            if (body == null) {
                emit(Resource.Error("Empty response body"))
                return@flow
            }

            // Stream directly to file (memory efficient for large files)
            val contentLength = body.contentLength()
            var downloadedBytes = 0L

            FileOutputStream(targetFile).use { fileOutput ->
                body.byteStream().use { inputStream ->
                    val buffer = ByteArray(8192) // 8KB chunks
                    var bytesRead: Int

                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        fileOutput.write(buffer, 0, bytesRead)
                        downloadedBytes += bytesRead

                        // Emit progress
                        if (contentLength > 0) {
                            val progress = (downloadedBytes * 100 / contentLength).toInt()
                            Log.d("DownloadModelUseCase", "Download progress: $progress%")
                        }
                    }
                }
            }

            // Verify download completed successfully
            if (targetFile.exists() && targetFile.length() > 0) {
                Log.d(
                    "DownloadModelUseCase",
                    "Download complete. File: ${targetFile.absolutePath}, Size: ${targetFile.length()} bytes"
                )
                emit(Resource.Success(targetFile.absolutePath))
            } else {
                emit(Resource.Error("File verification failed"))
            }

        } catch (e: Exception) {
            Log.e("DownloadModelUseCase", "Download failed: ${e.message}", e)
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)  // Run on IO thread

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