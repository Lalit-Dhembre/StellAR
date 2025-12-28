package com.cosmic_struck.stellar.home.domain.usecases

import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.data.stellar.repository.classroomRepo.ClassroomRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class JoinClassroomUseCase @Inject constructor(
    private val repository: ClassroomRepo
) {
    operator fun invoke(joinCode: String, userId: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.joinClassroom(joinCode, userId)
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown Error"))
            emit(Resource.Success(false))
        }
    }
}