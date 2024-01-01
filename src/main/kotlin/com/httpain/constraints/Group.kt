package com.httpain.constraints

import ai.timefold.solver.core.api.domain.entity.PlanningEntity
import ai.timefold.solver.core.api.domain.lookup.PlanningId
import ai.timefold.solver.core.api.domain.variable.PlanningVariable

@PlanningEntity
@NoArgConstructor
data class Group(
    @PlanningId
    val groupNumber: Int
) {

    @PlanningVariable(nullable = true)
    var tank: Character? = null

    @PlanningVariable(nullable = true)
    var healer: Character? = null

    @PlanningVariable(nullable = true)
    var dps1: Character? = null

    @PlanningVariable(nullable = true)
    var dps2: Character? = null

    @PlanningVariable(nullable = true)
    var dps3: Character? = null

    fun dps(): List<Character?> = listOf(dps1, dps2, dps3)
    fun members(): List<Character?> = listOf(tank, healer) + dps()

}