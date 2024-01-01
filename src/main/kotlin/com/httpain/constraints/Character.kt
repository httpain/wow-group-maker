package com.httpain.constraints

import ai.timefold.solver.core.api.domain.entity.PlanningEntity
import ai.timefold.solver.core.api.domain.variable.PlanningVariable

@PlanningEntity
@NoArgConstructor
data class Character(val name: String, val role: Role, val keyLevel: Int) {

    @PlanningVariable(nullable = true)
    var group: Group? = null
}