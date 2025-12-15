package com.cosmic_struck.stellar.stellar.models.domain.model

data class Planet(
    val planet_id: String,
    val planet_name: String,
    val description: String,
    val rarity: String,
    val planet_thumbnail: String,
    val planet_xp: Int
)
