package com.cosmic_struck.stellar.data.stellar.repository.classroomRepo

import com.cosmic_struck.stellar.data.remote.dto.ModelDTO
import com.cosmic_struck.stellar.data.stellar.remote.StellARAPI
import com.cosmic_struck.stellar.data.stellar.remote.dto.ClassroomDTOItem
import com.cosmic_struck.stellar.data.stellar.remote.dto.ClassroomMemberDTO
import com.cosmic_struck.stellar.data.stellar.remote.dto.JoinClassroomResponseDTO
import javax.inject.Inject

class ClassroomRepoImpl @Inject constructor(
    private val api: StellARAPI
): ClassroomRepo {
    override suspend fun getClassroomMembers(classroom_id: String): List<ClassroomMemberDTO> {
        return api.getClassroomMembers(classroom_id)
    }

    override suspend fun getClassroom(classroom_id: String): ClassroomDTOItem{
        return api.getClassroom(classroom_id)
    }

    override suspend fun getClassroomModelList(classroom_id: String): List<ModelDTO> {
        return api.getClassroomModels(classroom_id = classroom_id)
    }

    override suspend fun joinClassroom(
        classroom_id: String,
        user_id: String
    ): JoinClassroomResponseDTO {
        return api.joinClassroom(classroom_id, user_id)
    }
}