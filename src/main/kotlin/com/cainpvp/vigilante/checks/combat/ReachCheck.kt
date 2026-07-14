package com.cainpvp.vigilante.checks.combat

import com.cainpvp.vigilante.Vigilante
import com.cainpvp.vigilante.checks.Check
import net.minestom.server.collision.BoundingBox
import net.minestom.server.component.DataComponents
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.entity.EntityAttackEvent
import net.minestom.server.event.trait.InstanceEvent
import kotlin.math.min

class ReachCheck(vigilante: Vigilante) : Check(vigilante, "Reach") {
    companion object {
        private const val MAX_REACH = 3.0f
    }

    override fun createNode(): EventNode<InstanceEvent> {
        val node = EventNode.type("vigilante-reach", EventFilter.INSTANCE)

        node.addListener(EntityAttackEvent::class.java) { event ->
            if (event.entity is Player && event.target is Player) {
                val player = event.entity as Player

                if (vigilante.isConsidered(player)) {
                    val target = event.target as Player
                    var maxReach = MAX_REACH
                    val itemInMainHand = player.itemInMainHand

                    if (itemInMainHand.has(DataComponents.ATTACK_RANGE)) {
                        maxReach = itemInMainHand.get(DataComponents.ATTACK_RANGE)!!.maxReach
                    }

                    val eyePos = player.position.add(0.0, player.eyeHeight, 0.0)
                    val closestPoint = getClosestPointOnHitbox(eyePos, target.position, target.boundingBox)
                    val realDistance = eyePos.distance(closestPoint)

                    if (realDistance > maxReach) {
                        val overshoot = (realDistance - maxReach).toFloat()
                        val severity = 1f + overshoot * 3

                        flag(player.uuid, severity)
                    }
                }
            }
        }

        return node
    }

    private fun getClosestPointOnHitbox(eyePos: Pos, targetPos: Pos, box: BoundingBox): Pos {
        val minX = targetPos.x() + box.minX()
        val maxX = targetPos.x() + box.maxX()
        val minY = targetPos.y() + box.minY()
        val maxY = targetPos.y() + box.maxY()
        val minZ = targetPos.z() + box.minZ()
        val maxZ = targetPos.z() + box.maxZ()

        val closestX = eyePos.x().coerceIn(minX, maxX)
        val closestY = eyePos.y().coerceIn(minY, maxY)
        val closestZ = eyePos.z().coerceIn(minZ, maxZ)

        return Pos(closestX, closestY, closestZ)
    }
}