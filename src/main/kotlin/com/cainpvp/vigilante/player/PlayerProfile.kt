package com.cainpvp.vigilante.player

import com.cainpvp.vigilante.checks.Check
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max
import kotlin.reflect.KClass

data class PlayerProfile(val uuid: UUID) {
    val violations = ConcurrentHashMap<KClass<out Check>, Float>()

    fun addViolation(checkClass: KClass<out Check>, amount: Float): Float {
        val currentVl = violations.getOrDefault(checkClass, 0f)
        val newVl = currentVl + amount

        violations[checkClass] = newVl

        return newVl
    }

    fun decayViolations(amount: Float) {
        violations.forEach { (checkClass, vl) ->
            if (vl > 0f) {
                violations[checkClass] = max(0f, vl - amount)
            }
        }
    }
}