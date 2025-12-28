package com.cosmic_struck.stellar.data.stellar.repository.classroomRepo

import com.cosmic_struck.stellar.data.remote.dto.ModelDTO
import com.cosmic_struck.stellar.data.stellar.remote.dto.ClassroomDTOItem
import com.cosmic_struck.stellar.data.stellar.remote.dto.ClassroomMemberDTO
import com.cosmic_struck.stellar.data.stellar.remote.dto.JoinClassroomResponseDTO

interface ClassroomRepo {
    suspend fun getClassroomMembers(classroom_id: String): List<ClassroomMemberDTO>

    suspend fun getClassroom(classroom_id: String): ClassroomDTOItem

    suspend fun getClassroomModelList(classroom_id: String): List<ModelDTO>
    suspend fun joinClassroom(classroom_id: String, user_id: String): JoinClassroomResponseDTO
}