package com.cosmic_struck.stellar.stellar.models.domain.usecase

import android.net.http.HttpException
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.data.remote.dto.toPlanet
import com.cosmic_struck.stellar.data.repository.modelsRepo.ModelsScreenRepo
import com.cosmic_struck.stellar.stellar.models.domain.model.Planet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetModelsListUseCase @Inject constructor(
    private val modelsScreenRepo: ModelsScreenRepo
) {
    operator fun invoke() : Flow<Resource<List<Planet>>> = flow {
        try {
            emit(Resource.Loading())
            val planets = modelsScreenRepo.getModelsList().planets.map { it.toPlanet() }
            emit(Resource.Success(planets))
        }catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}