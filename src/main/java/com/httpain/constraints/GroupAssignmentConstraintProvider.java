package com.httpain.constraints;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GroupAssignmentConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                rolesMustMatch(constraintFactory),
                playersMustNotRepeatInsideGroup(constraintFactory),
                playersMustNotRepeatBetweenGroups(constraintFactory),
//                groupMustHaveAtLeastOnePlayer(constraintFactory),
                shouldMatchKeyRanges(constraintFactory),
                penalizeEmptySlots(constraintFactory)
        };
    }

    private Constraint rolesMustMatch(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingNullVars(Group.class)
                .filter(group -> (group.getTank() != null && group.getTank().getRole() != Role.Tank)
                        || (group.getHealer() != null && group.getHealer().getRole() != Role.Healer)
                        || group.dps().stream().filter(Objects::nonNull).anyMatch(dps -> dps.getRole() != Role.Dps))
                .penalize(HardSoftScore.ofHard(1000))
                .asConstraint("Roles must match");
    }

    private Constraint playersMustNotRepeatInsideGroup(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingNullVars(Group.class)
                .filter(group -> group.members().stream().filter(Objects::nonNull).distinct().count() < group.members().stream().filter(Objects::nonNull).count())
                .penalize(HardSoftScore.ofHard(1000))
                .asConstraint("Players must not repeat inside group");
    }

    private Constraint playersMustNotRepeatBetweenGroups(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingNullVars(Group.class)
                .join(constraintFactory
                        .forEachIncludingNullVars(Group.class))
                .filter((a, b) -> a.getGroupNumber() < b.getGroupNumber() && !groupsHaveDistinctPlayers(a, b))
                .penalize(HardSoftScore.ofHard(1000))
                .asConstraint("Players must not repeat between groups");
    }

    private Constraint groupMustHaveAtLeastOnePlayer(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingNullVars(Group.class)
                .filter(group -> group.members().stream().allMatch(Objects::isNull))
                .penalize(HardSoftScore.ofHard(1000))
                .asConstraint("No empty groups");
    }

    private Constraint penalizeEmptySlots(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingNullVars(Group.class)
//                .filter(group -> !group.members().stream().filter(Objects::nonNull).toList().isEmpty() && group.members().contains(null))
                .filter(group -> group.members().contains(null))
                .penalize(HardSoftScore.ofSoft(10), group -> Math.toIntExact(group.members().stream().filter(Objects::isNull).count()))
                .asConstraint("Penalize empty slots");
    }

    private Constraint shouldMatchKeyRanges(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingNullVars(Group.class)
                .filter(group -> group.members().stream().anyMatch(Objects::nonNull) && group.members().stream().filter(Objects::nonNull).map(character -> character.getKeyLevel()).collect(Collectors.toSet()).size() <= 1)
                .reward(HardSoftScore.ofSoft(10000))
                .asConstraint("Key ranges should match");
    }

    private boolean groupsHaveDistinctPlayers(Group a, Group b) {
        List<String> nonNullACharacters = a.members().stream().filter(Objects::nonNull).map(Character::getName).toList();
        List<String> nonNullBCharacters = b.members().stream().filter(Objects::nonNull).map(Character::getName).toList();

        return nonNullACharacters.stream().noneMatch(nonNullBCharacters::contains);
    }

}
