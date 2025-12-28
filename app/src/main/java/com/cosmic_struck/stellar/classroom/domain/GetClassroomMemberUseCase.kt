package com.cosmic_struck.stellar.classroom.domain

import android.util.Log
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.data.stellar.remote.dto.ClassroomMemberDTO
import com.cosmic_struck.stellar.data.stellar.repository.classroomRepo.ClassroomRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetClassroomMemberUseCase @Inject constructor(
    private val classroomRepo: ClassroomRepo
) {
    operator fun invoke(classroom_id: String) : Flow<Resource<List<ClassroomMemberDTO>>> = flow {
        try {
            emit(Resource.Loading())
            val classroomMembers = classroomRepo.getClassroomMembers(classroom_id)
            emit(Resource.Success(classroomMembers))
        } catch (e: Exception) {
            Log.e("GetClassroomMemberUseCase", e.message.toString())
            emit(Resource.Error(e.message.toString()))
        }
    }
}