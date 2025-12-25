package com.cosmic_struck.stellar.data.stellar.repository.homeRepo

import com.cosmic_struck.stellar.data.stellar.remote.dto.ClassroomDTOItem
import com.cosmic_struck.stellar.data.stellar.remote.dto.JoinedClassroomDTO

interface HomeRepo {

    suspend fun getClassrooms(userId: String): List<ClassroomDTOItem>

    suspend fun getJoinedClassrooms(userId: String): List<JoinedClassroomDTO>
}