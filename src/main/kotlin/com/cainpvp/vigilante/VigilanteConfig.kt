package com.cainpvp.vigilante

import java.util.UUID

data class VigilanteConfig(
    val ignoredPlayers: Set<UUID> = setOf(),
    val kickThreshold: Float = Float.MAX_VALUE,
    val banThreshold: Float = Float.MAX_VALUE
)