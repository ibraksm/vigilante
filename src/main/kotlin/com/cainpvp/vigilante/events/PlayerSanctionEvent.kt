package com.cainpvp.vigilante.events

import com.cainpvp.vigilante.SanctionType
import net.minestom.server.entity.Player
import net.minestom.server.event.trait.PlayerEvent

data class PlayerSanctionEvent(private val player: Player, val sanctionType: SanctionType): PlayerEvent {
    override fun getPlayer(): Player {
        return player
    }
}
