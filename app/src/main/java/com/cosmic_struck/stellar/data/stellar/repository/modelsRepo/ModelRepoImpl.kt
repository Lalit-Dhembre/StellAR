package com.cosmic_struck.stellar.data.repository.modelsRepo

import com.cosmic_struck.stellar.data.remote.dto.ModelDTO
import com.cosmic_struck.stellar.data.stellar.remote.StellARAPI
import javax.inject.Inject

class ModelRepoImpl @Inject constructor(
    private val api: StellARAPI
) : ModelsScreenRepo {
    override suspend fun getModelsList(): List<ModelDTO> {
        return api.getModels()
    }

    override suspend fun getModelURL(id:String): String {
        return api.getUrl(id).model_url
    }

}