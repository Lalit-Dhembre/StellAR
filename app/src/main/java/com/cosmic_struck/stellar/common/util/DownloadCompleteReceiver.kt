package com.cosmic_struck.stellar.common.util

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.io.File

class DownloadCompleteReceiver : BroadcastReceiver() {
    companion object {
        private var currentDownloadId = -1L
        private var currentFilePath = ""
        private val downloadResultFlow = MutableSharedFlow<Resource<String>>(replay = 1)

        fun setDownloadInfo(downloadId: Long, filePath: String) {
            currentDownloadId = downloadId
            currentFilePath = filePath
            Log.d("DownloadCompleteReceiver", "Download info set - ID: $downloadId, Path: $filePath")
        }

        fun getDownloadResult(): Flow<Resource<String>> = downloadResultFlow

        private fun emitResult(result: Resource<String>) {
            downloadResultFlow.tryEmit(result)
            Log.d("DownloadCompleteReceiver", "Result emitted: $result")
        }

        fun emitSuccess(filePath: String) {
            emitResult(Resource.Success(filePath))
        }

        fun emitError(message: String) {
            emitResult(Resource.Error(message))
        }

        fun reset() {
            currentDownloadId = -1L
            currentFilePath = ""
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)

            if (id != -1L && id == currentDownloadId) {
                Log.d("DownloadCompleteReceiver", "Download broadcast received for ID: $id")

                // Query download status
                val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
                val query = DownloadManager.Query().setFilterById(id)
                val cursor = downloadManager?.query(query)

                if (cursor?.moveToFirst() == true) {
                    val statusColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    val status = cursor.getInt(statusColumnIndex)

                    Log.d("DownloadCompleteReceiver", "Download status: $status")

                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            Log.d("DownloadCompleteReceiver", "Download successful for ID: $id")

                            // Get file path and verify
                            val fileUriColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                            val fileUri = cursor.getString(fileUriColumnIndex)
                            val file = fileUri?.let { Uri.parse(it).path }?.let { File(it) }

                            if (file?.exists() == true && file.length() > 0) {
                                Log.d(
                                    "DownloadCompleteReceiver",
                                    "File verified: ${file.absolutePath}, Size: ${file.length()} bytes"
                                )
                                emitSuccess(file.absolutePath)
                            } else {
                                Log.e("DownloadCompleteReceiver", "File not found or empty after download")
                                emitError("File not found or empty after download")
                            }

                            reset()
                        }

                        DownloadManager.STATUS_FAILED -> {
                            val reasonColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
                            val reason = cursor.getInt(reasonColumnIndex)

                            val reasonText = when (reason) {
                                DownloadManager.ERROR_CANNOT_RESUME -> "Cannot resume"
                                DownloadManager.ERROR_DEVICE_NOT_FOUND -> "Device not found"
                                DownloadManager.ERROR_FILE_ALREADY_EXISTS -> "File already exists"
                                DownloadManager.ERROR_HTTP_DATA_ERROR -> "HTTP data error"
                                DownloadManager.ERROR_INSUFFICIENT_SPACE -> "Insufficient space"
                                DownloadManager.ERROR_TOO_MANY_REDIRECTS -> "Too many redirects"
                                DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> "Unhandled HTTP code"
                                DownloadManager.ERROR_UNKNOWN -> "Unknown error"
                                else -> "Reason: $reason"
                            }

                            Log.e("DownloadCompleteReceiver", "Download failed for ID: $id - $reasonText")
                            emitError("Download failed: $reasonText")

                            reset()
                        }

                        DownloadManager.STATUS_RUNNING -> {
                            val bytesDownloadedColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                            val totalSizeColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)

                            val bytesDownloaded = cursor.getLong(bytesDownloadedColumnIndex)
                            val totalSize = cursor.getLong(totalSizeColumnIndex)

                            if (totalSize > 0) {
                                val progress = (bytesDownloaded * 100 / totalSize).toInt()
                                Log.d("DownloadCompleteReceiver", "Download progress: $progress% ($bytesDownloaded/$totalSize bytes)")
                            }
                        }

                        DownloadManager.STATUS_PENDING -> {
                            Log.d("DownloadCompleteReceiver", "Download pending for ID: $id")
                        }

                        DownloadManager.STATUS_PAUSED -> {
                            val reasonColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
                            val reason = cursor.getInt(reasonColumnIndex)

                            val reasonText = when (reason) {
                                DownloadManager.PAUSED_WAITING_FOR_NETWORK -> "Waiting for network"
                                DownloadManager.PAUSED_WAITING_TO_RETRY -> "Waiting to retry"
                                else -> "Reason: $reason"
                            }

                            Log.d("DownloadCompleteReceiver", "Download paused for ID: $id - $reasonText")
                        }
                    }
                    cursor.close()
                }
            } else if (id != currentDownloadId) {
                Log.d("DownloadCompleteReceiver", "Received broadcast for different download ID: $id (expected: $currentDownloadId)")
            }
        }
    }
}