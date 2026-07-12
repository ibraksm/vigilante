package com.cainpvp.vigilante.checks.movement

import com.cainpvp.vigilante.Vigilante
import com.cainpvp.vigilante.checks.Check
import net.minestom.server.collision.CollisionUtils
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerMoveEvent
import net.minestom.server.event.trait.InstanceEvent
import net.minestom.server.tag.Tag
import kotlin.math.abs
import kotlin.math.max

class FlightCheck(vigilante: Vigilante) : Check(vigilante, "Flight") {
    companion object {
        private val VL = Tag.Float("vigilante_flight_vl").defaultValue(0f)
    }

    private val baseTolerance = 0.05
    private val maxVlThreshold = 1.5

    override fun createNode(): EventNode<InstanceEvent> {
        val node = EventNode.type("vigilante-flight", EventFilter.INSTANCE)

        node.addListener(PlayerMoveEvent::class.java) { event ->
            if (!event.isOnGround) {
                val player = event.player

                if (vigilante.isConsidered(player) && !player.isAllowFlying) {
                    val position: Pos = player.position
                    val boundingBox = player.boundingBox
                    val velocityPerTick = player.velocity.div(20.0)

                    val physicsResult = CollisionUtils.handlePhysics(
                        player.instance!!,
                        player.chunk,
                        boundingBox,
                        position,
                        velocityPerTick,
                        null,
                        false
                    )

                    val predictedNextPos: Pos = physicsResult.newPosition
                    val difference = abs(event.newPosition.y - predictedNextPos.y).toFloat()

                    var vl = player.getTag(VL)

                    if (difference > baseTolerance) {
                        vl += difference
                    } else {
                        vl = max(0f, vl - 0.1f)
                    }

                    if (vl > maxVlThreshold) {
                        flag(player.uuid, vl)
                        event.isCancelled = true
                    }

                    player.setTag(VL, vl)
                }
            } else {
                event.player.setTag(VL, 0f)
            }
        }

        return node
    }
}