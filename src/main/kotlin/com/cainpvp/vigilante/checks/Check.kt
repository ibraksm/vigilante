package com.cainpvp.vigilante.checks

import com.cainpvp.vigilante.Vigilante
import com.cainpvp.vigilante.events.PlayerFlagEvent
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventDispatcher
import net.minestom.server.event.EventNode
import net.minestom.server.event.trait.InstanceEvent
import java.util.UUID

abstract class Check(
    protected val vigilante: Vigilante,
    val displayName: String
) {
    abstract fun createNode(): EventNode<InstanceEvent>

    fun flag(uuid: UUID, severity: Float = 1f) {
        val player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid) ?: return

        val profile = vigilante.getPlayerProfile(uuid)
        val vl = profile.addViolation(this::class, severity)
        val playerFlagEvent = PlayerFlagEvent(player, displayName, vl)

        EventDispatcher.call(playerFlagEvent)
    }
}