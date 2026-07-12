package com.cainpvp.vigilante.checks.movement

import com.cainpvp.vigilante.Vigilante
import com.cainpvp.vigilante.checks.Check
import net.minestom.server.collision.BoundingBox
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerMoveEvent
import net.minestom.server.event.trait.InstanceEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import kotlin.math.floor

class NoFallCheck(vigilante: Vigilante): Check(vigilante, "No Fall") {
    private val minimumDeltaY = -0.1

    override fun createNode(): EventNode<InstanceEvent> {
        val node = EventNode.type("vigilante-no_fall", EventFilter.INSTANCE)

        node.addListener(PlayerMoveEvent::class.java) { event ->
            val player = event.player
            val newPosition = event.newPosition
            val positionY = player.position.y
            val newPositionY = newPosition.y

            if (positionY != newPositionY) {
                val deltaY = newPositionY - positionY
                val blocksUnderFeet = getBlocksUnderFeet(player, newPosition)
                val hasGround = blocksUnderFeet.any { it.isSolid }

                if (event.isOnGround && !hasGround && deltaY <= minimumDeltaY) {
                    flag(player.uuid, 95f)
                }
            }
        }

        return node
    }

    private fun getBlocksUnderFeet(player: Player, point: Point): List<Block> {
        val instance = player.instance ?: return emptyList()

        val minX = floor(point.x() - 0.3).toInt()
        val maxX = floor(point.x() + 0.3).toInt()

        val minY = floor(point.y() - 0.15).toInt()

        val minZ = floor(point.z() - 0.3).toInt()
        val maxZ = floor(point.z() + 0.3).toInt()

        val blocks = mutableListOf<Block>()

        for (x in minX..maxX) {
            for (z in minZ..maxZ) {
                blocks.add(instance.getBlock(x, minY, z))
            }
        }

        return blocks
    }
}