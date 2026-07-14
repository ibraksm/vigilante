package com.cainpvp.vigilante

import com.cainpvp.vigilante.events.*
import com.cainpvp.vigilante.player.PlayerProfile
import com.cainpvp.vigilante.sanction.SanctionType
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.EventDispatcher
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class Vigilante(
    private val config: VigilanteConfig
) {
    private val playerProfiles = ConcurrentHashMap<UUID, PlayerProfile>()

    init {
        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent::class.java) {
            getPlayerProfile(it.player.uuid)
        }

        MinecraftServer.getGlobalEventHandler().addListener(PlayerFlagEvent::class.java) { event ->
            val player = event.player
            val vl = event.vl

            if (vl >= config.banThreshold) {
                val banEvent = PlayerSanctionEvent(player, SanctionType.BAN)
                EventDispatcher.call(banEvent)
            } else if (vl >= config.kickThreshold) {
                val kickEvent = PlayerSanctionEvent(player, SanctionType.KICK)
                EventDispatcher.call(kickEvent)
            }
        }
    }

    fun getPlayerProfile(uuid: UUID): PlayerProfile {
        return playerProfiles.computeIfAbsent(uuid) { PlayerProfile(uuid) }
    }

    fun isConsidered(player: Player): Boolean {
        return if (config.ignoredPlayers.contains(player.uuid)) {
            false
        } else {
            player.gameMode != GameMode.CREATIVE && player.gameMode != GameMode.SPECTATOR
        }
    }
}