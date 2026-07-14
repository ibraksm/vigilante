package com.cainpvp.vigilante.events

import net.minestom.server.entity.Player
import net.minestom.server.event.trait.PlayerEvent

data class PlayerFlagEvent(
    private val player: Player,
    val checkDisplayName: String,
    val vl: Float,
    val details: String = ""
) : PlayerEvent {
    override fun getPlayer(): Player {
        return player
    }
}
