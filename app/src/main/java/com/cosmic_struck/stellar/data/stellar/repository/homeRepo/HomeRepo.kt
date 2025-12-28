package com.cosmic_struck.stellar.data.stellar.repository.homeRepo

import com.cosmic_struck.stellar.data.stellar.remote.dto.ClassroomDTOItem
import com.cosmic_struck.stellar.data.stellar.remote.dto.JoinedClassroomDTO
import com.cosmic_struck.stellar.data.stellar.remote.dto.UserDTO

interface HomeRepo {

    suspend fun getJoinedClassrooms(userId: String): List<JoinedClassroomDTO>

    suspend fun getUser(userId: String): UserDTO
}