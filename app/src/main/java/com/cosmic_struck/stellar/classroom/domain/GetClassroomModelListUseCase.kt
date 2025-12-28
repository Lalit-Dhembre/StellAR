package com.cosmic_struck.stellar.classroom.domain

import android.util.Log
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.data.remote.dto.ModelDTO
import com.cosmic_struck.stellar.data.stellar.repository.classroomRepo.ClassroomRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetClassroomModelListUseCase @Inject constructor(
    private val classroomRepo: ClassroomRepo
) {
    operator fun invoke(classroom_id: String) : Flow<Resource<List<ModelDTO>>> = flow {
        try {
            emit(Resource.Loading())
            val models = classroomRepo.getClassroomModelList(classroom_id)
            emit(Resource.Success(models))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            Log.d("GetClassroomModelUsecase", e.localizedMessage ?: "An unexpected error occurred")
        }
    }
}