package com.httpain.constraints

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty
import org.optaplanner.core.api.domain.solution.PlanningScore
import org.optaplanner.core.api.domain.solution.PlanningSolution
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore

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