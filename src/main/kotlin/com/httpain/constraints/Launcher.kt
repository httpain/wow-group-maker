package com.httpain.constraints

import ai.timefold.solver.core.api.solver.SolverFactory
import ai.timefold.solver.core.config.solver.SolverConfig
import ai.timefold.solver.core.config.solver.termination.TerminationConfig

private val characters = listOf(
    Character("Alice", Role.Healer, 10),
    Character("Bob", Role.Tank, 20),
    Character("Carol", Role.Dps, 10),
//    Character("Dan", Role.Dps, 20),
//    Character("Eugene", Role.Healer, 20),
//    Character("Fergie", Role.Dps, 20),
//    Character("Harold", Role.Dps, 20),
//    Character("Igor", Role.Dps, 15),
//    Character("Jay", Role.Dps, 10),
//    Character("Kate", Role.Tank, 10),
//    Character("Leon", Role.Dps, 15),
    Character("Mike", Role.Tank, 10),
    Character("Nigel", Role.Tank, 10)
)

private val groups = (1..1).map { Group(it) }

fun main() {
    val solverFactory = SolverFactory.create<Assignment>(
        SolverConfig()
            .withSolutionClass(Assignment::class.java)
            .withEntityClasses(Character::class.java)
            .withConstraintProviderClass(GroupAssignmentConstraintProvider::class.java)
            .withTerminationConfig(
                TerminationConfig().withUnimprovedMillisecondsSpentLimit(1000)
            )
    )

    val problem = Assignment(characters, groups)
    val solver = solverFactory.buildSolver()

    val solution = solver.solve(problem)

    solution.groups.forEach { group ->
        println("----- Group ${group.groupNumber} -----")
        group.members.orEmpty().forEach { member ->
            if (member == null) {
                println(" null")
            } else {
                println(" ${member.role}: ${member.name} (${member.keyLevel})")
            }
        }
    }
}