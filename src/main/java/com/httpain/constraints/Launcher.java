package com.httpain.constraints;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Launcher {
    private static final List<Character> characters = Arrays.asList(
            new Character("Alice", Role.Healer, 10),
            new Character("Bob", Role.Tank, 20),
            new Character("Carol", Role.Dps, 10),
            new Character("Dan", Role.Dps, 20),
            new Character("Eugene", Role.Healer, 20),
            new Character("Fergie", Role.Dps, 20),
            new Character("Harold", Role.Dps, 20),
            new Character("Igor", Role.Dps, 15),
            new Character("Jay", Role.Dps, 10),
            new Character("Kate", Role.Tank, 10),
            new Character("Leon", Role.Dps, 15),
            new Character("Mike", Role.Tank, 10),
            new Character("Nigel", Role.Tank, 10)

    );

    private static final List<Group> groups = Stream.of(new Group(1), new Group(2), new Group(3), new Group(4), new Group(5)).collect(Collectors.toList());

    public static void main(String[] args) {
        SolverFactory<Assignment> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(Assignment.class)
                .withEntityClasses(Group.class)
                .withConstraintProviderClass(GroupAssignmentConstraintProvider.class)
                        .withTerminationConfig(new TerminationConfig().withUnimprovedMillisecondsSpentLimit(200L))
                /*.withTerminationSpentLimit(Duration.ofSeconds(30))*/);

        Assignment problem = new Assignment(characters, groups);

        Solver<Assignment> solver = solverFactory.buildSolver();
        Assignment solution = solver.solve(problem);

        solution.getGroups().forEach(group -> {
            System.out.printf("----- Group %d -----\n", group.getGroupNumber());
            group.members().forEach(member -> {
                if (member == null) {
                    System.out.print(" null\n");
                } else {
                    System.out.printf(" %s : %s (%s)\n", member.role, member.name, member.keyLevel);
                }
            });
        });
    }

}

