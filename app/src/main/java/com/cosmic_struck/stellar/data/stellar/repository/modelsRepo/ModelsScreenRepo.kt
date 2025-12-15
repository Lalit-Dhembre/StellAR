package com.cosmic_struck.stellar.data.repository.modelsRepo

import com.cosmic_struck.stellar.data.remote.dto.PlanetListDTO

interface ModelsScreenRepo {
    suspend fun getModelsList() : PlanetListDTO
    suspend fun getModelURL() : String
}
