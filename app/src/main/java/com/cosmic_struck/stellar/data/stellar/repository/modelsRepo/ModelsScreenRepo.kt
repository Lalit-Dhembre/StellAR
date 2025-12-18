package com.cosmic_struck.stellar.data.repository.modelsRepo

import com.cosmic_struck.stellar.data.remote.dto.PlanetDTO

interface ModelsScreenRepo {
    suspend fun getModelsList() : List<PlanetDTO>
    suspend fun getModelURL(id: String) : String
}
