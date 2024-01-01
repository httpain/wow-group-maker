package com.httpain.constraints

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore
import org.optaplanner.core.api.score.stream.Constraint
import org.optaplanner.core.api.score.stream.ConstraintFactory
import org.optaplanner.core.api.score.stream.ConstraintProvider

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

    private fun rolesMustMatch(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachIncludingNullVars(Group::class.java)
            .filter { group ->
                group.tank != null && group.tank!!.role != Role.Tank
                        || group.healer != null && group.healer!!.role != Role.Healer
                        || group.dps().filterNotNull().any { it.role != Role.Dps }
            }
            .penalize(HardSoftScore.ofHard(1000))
            .asConstraint("Roles must match")
    }

    private fun playersMustNotRepeatInsideGroup(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachIncludingNullVars(Group::class.java)
            .filter { group -> group.members().filterNotNull().hasRepeatingMembers() }
            .penalize(HardSoftScore.ofHard(1000))
            .asConstraint("Players must not repeat inside group");
    }

    private fun playersMustNotRepeatBetweenGroups(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachIncludingNullVars(Group::class.java)
            .join(constraintFactory.forEachIncludingNullVars(Group::class.java))
            .filter { it, other -> it.groupNumber < other.groupNumber && !groupsHaveDistinctPlayers(it, other) }
            .penalize(HardSoftScore.ofHard(1000))
            .asConstraint("Players must not repeat between groups")
    }

    private fun penalizeEmptySlots(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachIncludingNullVars(Group::class.java)
            .filter { it.members().contains(null) }
            .penalize(HardSoftScore.ofSoft(10)) { group -> group.members().count { it == null } }
            .asConstraint("Penalize empty slots")
    }

    private fun shouldMatchKeyRanges(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachIncludingNullVars(Group::class.java)
            .filter { group -> group.members().filterNotNull().isNotEmpty() && group.members().filterNotNull().map { it.keyLevel }.toSet().size <= 1 }
            .reward(HardSoftScore.ofSoft(10000))
            .asConstraint("Key ranges should match")
    }

    private fun groupsHaveDistinctPlayers(a: Group, b: Group): Boolean {
        return (a.members().filterNotNull() + b.members().filterNotNull()).hasRepeatingMembers().not()
    }

    private fun List<Any>.hasRepeatingMembers(): Boolean {
        return this.groupingBy { it }.eachCount().any { it.value > 1 }
    }


}