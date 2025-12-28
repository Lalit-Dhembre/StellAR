package com.cosmic_struck.stellar.home.domain.repository

import com.cosmic_struck.stellar.data.stellar.remote.dto.JoinedClassroomDTO
import com.cosmic_struck.stellar.data.stellar.remote.dto.UserDTO

interface HomeRepository {
    suspend fun joinClassroom(code: String, userId: String): Boolean

    suspend fun getJoinedClassroom(userId: String): List<JoinedClassroomDTO>

    suspend fun getUser(userId: String): UserDTO

}