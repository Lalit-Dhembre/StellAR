package com.cosmic_struck.stellar.home.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val user_name: String,
    val level: Int,
    val total_xp: Int,
)
