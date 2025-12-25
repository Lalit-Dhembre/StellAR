package com.cosmic_struck.stellar.data.stellar.repository.homeRepo

import com.cosmic_struck.stellar.data.stellar.remote.StellARAPI
import com.cosmic_struck.stellar.data.stellar.remote.dto.ClassroomDTOItem
import com.cosmic_struck.stellar.data.stellar.remote.dto.JoinedClassroomDTO
import javax.inject.Inject

class HomeRepoImpl @Inject constructor(
    private val api: StellARAPI
): HomeRepo {
    override suspend fun getClassrooms(userId: String): List<ClassroomDTOItem> {
        return api.getClassroom(userId)
    }

    override suspend fun getJoinedClassrooms(userId: String): List<JoinedClassroomDTO> {
        return api.getJoinedClassrooms(userId)
    }
}