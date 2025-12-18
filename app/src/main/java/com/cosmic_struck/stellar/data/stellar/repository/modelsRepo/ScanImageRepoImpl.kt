package com.cosmic_struck.stellar.data.stellar.repository.modelsRepo

import com.cosmic_struck.stellar.data.remote.StellARAPI
import com.cosmic_struck.stellar.data.stellar.remote.dto.ScanDTO
import okhttp3.MultipartBody
import javax.inject.Inject

class ScanImageRepoImpl @Inject constructor(
    private val api: StellARAPI
) : ScanImageRepo {
    override suspend fun uploadImageToServer(image: MultipartBody.Part): ScanDTO {
        return api.getScanResults(image)
    }
}