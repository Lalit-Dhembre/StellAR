package com.cosmic_struck.stellar.classroom.domain

import android.util.Log
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.data.stellar.remote.dto.ClassroomDTOItem
import com.cosmic_struck.stellar.data.stellar.repository.classroomRepo.ClassroomRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetClassroomDetailsUseCase @Inject constructor(
    private val classroomRepo: ClassroomRepo
) {
    operator fun invoke(classroom_id : String) : Flow<Resource<ClassroomDTOItem>> = flow {
        try {
            emit(Resource.Loading())
            val classroom = classroomRepo.getClassroom(classroom_id)
            emit(Resource.Success(classroom))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            Log.e("GetClassroomDetailsUseCase", e.message.toString())
        }
    }
}