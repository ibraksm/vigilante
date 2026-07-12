package com.cainpvp.vigilante.checks

import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.trait.InstanceEvent

class CheckSet(
    private vararg val nodes: Check
) {
    fun build(): EventNode<InstanceEvent> {
        val node = EventNode.type("vigilante_check_nodes", EventFilter.INSTANCE)

        nodes.forEach {
            node.addChild(it.createNode())
        }

        return node
    }
}