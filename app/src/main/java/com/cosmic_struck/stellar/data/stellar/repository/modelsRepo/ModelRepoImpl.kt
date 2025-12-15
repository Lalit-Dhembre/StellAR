package com.cosmic_struck.stellar.data.repository.modelsRepo

import com.cosmic_struck.stellar.data.remote.StellARAPI
import com.cosmic_struck.stellar.data.remote.dto.PlanetListDTO
import javax.inject.Inject

class ModelRepoImpl @Inject constructor(
    private val api: StellARAPI
) : ModelsScreenRepo {
    override suspend fun getModelsList(): PlanetListDTO {
        return api.getModels()
    }

    override suspend fun getModelURL(): String {
        return api.getUrl()
    }

}