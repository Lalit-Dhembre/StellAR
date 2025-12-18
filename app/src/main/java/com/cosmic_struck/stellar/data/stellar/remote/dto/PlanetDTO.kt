package com.cosmic_struck.stellar.data.remote.dto

import com.cosmic_struck.stellar.stellar.models.domain.model.Planet

data class PlanetDTO(
    val model_id: String,
    val model_name: String,
    val description: String?,
    val rarity: String,
    val model_thumbnail: String,
    val xp_reward: Int,
    val min_level: Int,
)

fun PlanetDTO.toPlanet(): Planet {
    return Planet(
        planet_id = model_id,
        planet_name = model_name,
        description = description,
        rarity = rarity,
        planet_thumbnail = model_thumbnail,
        planet_xp = xp_reward,
        min_level = min_level
    )
}

