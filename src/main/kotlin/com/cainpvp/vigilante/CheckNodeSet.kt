package com.cainpvp.vigilante

import com.cainpvp.vigilante.nodes.CheckNode
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.trait.InstanceEvent

class CheckNodeSet(
    private vararg val nodes: CheckNode
) {
    fun build(): EventNode<InstanceEvent> {
        val node = EventNode.type("vigilante_nodes", EventFilter.INSTANCE)

        nodes.forEach {
            node.addChild(it.createNode())
        }

        return node
    }
}