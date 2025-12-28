package com.cosmic_struck.stellar.data.stellar.remote.dto

class JoinClassroomResponseDTO : ArrayList<JoinClassroomResponseDTOItem>()
data class JoinClassroomResponseDTOItem(
    val classroom_id: String,
    val id: String,
    val joined_at: String,
    val user_id: String
)