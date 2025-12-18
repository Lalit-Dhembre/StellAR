package com.cosmic_struck.stellar.data.stellar.repository.modelsRepo

import com.cosmic_struck.stellar.data.stellar.remote.dto.ScanDTO
import okhttp3.MultipartBody

interface ScanImageRepo {
    suspend fun uploadImageToServer(
        image: MultipartBody.Part
    ) : ScanDTO
}