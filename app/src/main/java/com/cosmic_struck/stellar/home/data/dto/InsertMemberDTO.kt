package com.cosmic_struck.stellar.home.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class InsertMemberDTO(
    val classroom_id: String,
    val user_id: String
)
