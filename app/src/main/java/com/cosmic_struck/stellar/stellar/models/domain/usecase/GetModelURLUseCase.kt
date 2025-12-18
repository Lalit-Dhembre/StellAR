package com.cosmic_struck.stellar.stellar.models.domain.usecase

import android.util.Log
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.data.remote.dto.toPlanet
import com.cosmic_struck.stellar.data.repository.modelsRepo.ModelsScreenRepo
import com.cosmic_struck.stellar.stellar.models.domain.model.Planet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetModelURLUseCase @Inject constructor(
    private val modelsScreenRepo: ModelsScreenRepo
) {
    operator fun invoke(
        id: String
    ) : Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val planets = modelsScreenRepo.getModelURL(id)
            Log.d("GET MODEL URL USE CASE", planets)
            emit(Resource.Success(planets))
        }catch (e: Exception){
            Log.d("GET MODEL URL USE CASE","${e.localizedMessage}")
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}