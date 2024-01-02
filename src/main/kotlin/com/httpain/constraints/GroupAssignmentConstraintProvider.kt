package com.httpain.constraints

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore
import ai.timefold.solver.core.api.score.stream.Constraint
import ai.timefold.solver.core.api.score.stream.ConstraintFactory
import ai.timefold.solver.core.api.score.stream.ConstraintProvider

class GroupAssignmentConstraintProvider : ConstraintProvider {
    override fun defineConstraints(constraintFactory: ConstraintFactory): Array<Constraint> {
        return arrayOf(
            rolesMustMatch(constraintFactory),
            playersMustNotRepeatInsideGroup(constraintFactory),
            playersMustNotRepeatBetweenGroups(constraintFactory),
            shouldMatchKeyRanges(constraintFactory),
            penalizeEmptySlots(constraintFactory)
        )
    }

    internal fun rolesMustMatch(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachIncludingNullVars(Group::class.java)
            .filter { group ->
                group.tank != null && group.tank!!.role != Role.Tank
                        || group.healer != null && group.healer!!.role != Role.Healer
                        || group.dps().filterNotNull().any { it.role != Role.Dps }
            }
            .penalize(HardSoftScore.ofHard(Scores.PENALTY_ON_ROLE_MISMATCH))
            .asConstraint("Roles must match")
    }

    internal fun playersMustNotRepeatInsideGroup(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachIncludingNullVars(Group::class.java)
            .filter { group -> group.members().filterNotNull().hasRepeatingMembers() }
            .penalize(HardSoftScore.ofHard(Scores.PENALTY_ON_DUPLICATE_PLAYERS_IN_GROUP))
            .asConstraint("Players must not repeat inside group");
    }

    internal fun playersMustNotRepeatBetweenGroups(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachIncludingNullVars(Group::class.java)
            .join(constraintFactory.forEachIncludingNullVars(Group::class.java))
            .filter { it, other -> it.groupNumber < other.groupNumber && !groupsHaveDistinctPlayers(it, other) }
            .penalize(HardSoftScore.ofHard(Scores.PENALTY_ON_DUPLICATE_PLAYERS_BETWEEN_GROUPS))
            .asConstraint("Players must not repeat between groups")
    }

    internal fun penalizeEmptySlots(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachIncludingNullVars(Group::class.java)
            .filter { it.members().contains(null) }
            .penalize(HardSoftScore.ofSoft(Scores.PENALTY_BASE_ON_EMPTY_SLOT)) { group -> group.members().count { it == null } }
            .asConstraint("Penalize empty slots")
    }

    internal fun shouldMatchKeyRanges(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachIncludingNullVars(Group::class.java)
            .filter { group -> group.members().filterNotNull().isNotEmpty() && group.members().filterNotNull().map { it.keyLevel }.toSet().size <= 1 }
            .reward(HardSoftScore.ofSoft(Scores.REWARD_ON_KEY_RANGE_MATCH))
            .asConstraint("Key ranges should match")
    }

    private fun groupsHaveDistinctPlayers(a: Group, b: Group): Boolean {
        return (a.members().filterNotNull() + b.members().filterNotNull()).hasRepeatingMembers().not()
    }

    private fun List<Any>.hasRepeatingMembers(): Boolean {
        return this.groupingBy { it }.eachCount().any { it.value > 1 }
    }


}