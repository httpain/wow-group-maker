package com.httpain.constraints

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore
import ai.timefold.solver.core.api.score.stream.Constraint
import ai.timefold.solver.core.api.score.stream.ConstraintFactory
import ai.timefold.solver.core.api.score.stream.ConstraintProvider

class GroupAssignmentConstraintProvider : ConstraintProvider {
    override fun defineConstraints(constraintFactory: ConstraintFactory): Array<Constraint> {
        return arrayOf(
//            rolesMustMatch(constraintFactory),
            playersMustNotRepeatInsideGroup(constraintFactory),
            playersMustNotRepeatBetweenGroups(constraintFactory),
            shouldMatchKeyRanges(constraintFactory),
            penalizeEmptySlots(constraintFactory),
            checkGroupSize(constraintFactory)
        )
    }

/*    private fun rolesMustMatch(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachIncludingNullVars(Group::class.java)
            .filter { group ->
                group.tank != null && group.tank!!.role != Role.Tank
                        || group.healer != null && group.healer!!.role != Role.Healer
                        || group.dps().filterNotNull().any { it.role != Role.Dps }
            }
            .penalize(HardSoftScore.ofHard(1000))
            .asConstraint("Roles must match")
    }*/

    private fun checkGroupSize(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachIncludingNullVars(Group::class.java)
            .filter { it.members.orEmpty().size > 5 || it.members.orEmpty().size < 2 }
            .penalize(HardSoftScore.ofHard(1000))
            .asConstraint("Too many group members")
    }

    private fun playersMustNotRepeatInsideGroup(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachIncludingNullVars(Group::class.java)
            .filter { group -> group.members.orEmpty().filterNotNull().hasRepeatingMembers() }
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
            .filter { it.members.orEmpty().contains(null) }
            .penalize(HardSoftScore.ofSoft(10)) { group -> group.members.orEmpty().count { it == null } }
            .asConstraint("Penalize empty slots")
    }

    private fun shouldMatchKeyRanges(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachIncludingNullVars(Group::class.java)
            .filter { group -> group.members.orEmpty().filterNotNull().isNotEmpty() && group.members.orEmpty().filterNotNull().map { it.keyLevel }.toSet().size <= 1 }
            .reward(HardSoftScore.ofSoft(10000))
            .asConstraint("Key ranges should match")
    }

    private fun groupsHaveDistinctPlayers(a: Group, b: Group): Boolean {
        return (a.members.orEmpty().filterNotNull() + b.members.orEmpty().filterNotNull()).hasRepeatingMembers().not()
    }

    private fun List<Any>.hasRepeatingMembers(): Boolean {
        return this.groupingBy { it }.eachCount().any { it.value > 1 }
    }


}