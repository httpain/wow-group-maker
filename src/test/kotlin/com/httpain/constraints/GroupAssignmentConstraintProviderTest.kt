package com.httpain.constraints

import ai.timefold.solver.test.api.score.stream.ConstraintVerifier
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class GroupAssignmentConstraintProviderTest {

    private val constraintVerifier = ConstraintVerifier.build(
        GroupAssignmentConstraintProvider(),
        Assignment::class.java,
        Group::class.java
    )

    @ParameterizedTest
    @MethodSource("groupsWithMismatchedRoles")
    fun `should penalize on role mismatch`(group: Group) {
        constraintVerifier.verifyThat(GroupAssignmentConstraintProvider::rolesMustMatch)
            .given(group)
            .penalizes()
    }

    companion object {
        @JvmStatic
        fun groupsWithMismatchedRoles(): List<Group> {
            return listOf(
                group().apply { tank = anyDps() },
                group().apply { tank = anyHealer() },
                group().apply { healer = anyTank() },
                group().apply { healer = anyDps() },
                group().apply { dps1 = anyTank() },
                group().apply { dps1 = anyHealer() },
                group().apply { dps2 = anyTank() },
                group().apply { dps2 = anyHealer() },
                group().apply { dps3 = anyTank() },
                group().apply { dps3 = anyHealer() }
            )
        }

        private fun group() = Group(1)

        private fun anyTank() = Character(name = "A", role = Role.Tank, keyLevel = 10)
        private fun anyHealer() = Character(name = "A", role = Role.Healer, keyLevel = 10)
        private fun anyDps() = Character(name = "A", role = Role.Dps, keyLevel = 10)

    }

}