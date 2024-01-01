package com.httpain.constraints

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty
import ai.timefold.solver.core.api.domain.solution.PlanningScore
import ai.timefold.solver.core.api.domain.solution.PlanningSolution
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore

@PlanningSolution
@NoArgConstructor
data class Assignment(
    @ValueRangeProvider
    @ProblemFactCollectionProperty
    val characters: List<Character>,

    @PlanningEntityCollectionProperty
    val groups: List<Group>,

    @PlanningScore
    var score: HardSoftScore? = null
)