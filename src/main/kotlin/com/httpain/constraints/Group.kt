package com.httpain.constraints

import ai.timefold.solver.core.api.domain.entity.PlanningEntity
import ai.timefold.solver.core.api.domain.lookup.PlanningId
import ai.timefold.solver.core.api.domain.variable.InverseRelationShadowVariable
import ai.timefold.solver.core.api.domain.variable.PlanningVariable

@PlanningEntity
@NoArgConstructor
data class Group(
    @PlanningId
    val groupNumber: Int
) {

    @InverseRelationShadowVariable(sourceVariableName = "group")
    var members: List<Character?>? = null

}