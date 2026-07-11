package com.cainpvp.vigilante.nodes

import com.cainpvp.vigilante.Vigilante
import net.minestom.server.event.EventNode
import net.minestom.server.event.trait.InstanceEvent

sealed class CheckNode(
    protected val vigilante: Vigilante
) {
    abstract fun createNode(): EventNode<InstanceEvent>
}