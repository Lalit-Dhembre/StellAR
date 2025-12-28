package com.cosmic_struck.stellar.data.repository.modelsRepo

import com.cosmic_struck.stellar.data.remote.dto.ModelDTO

interface ModelsScreenRepo {
    suspend fun getModelsList() : List<ModelDTO>
    suspend fun getModelURL(id: String) : String
}
