package com.cainpvp.vigilante

import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import java.util.UUID

class Vigilante(
    val bypassedPlayers: Set<UUID>
) {
    fun isBypassed(player: Player): Boolean {
        return bypassedPlayers.contains(player.uuid)
                || player.gameMode == GameMode.CREATIVE
                || player.gameMode == GameMode.SPECTATOR
    }
}