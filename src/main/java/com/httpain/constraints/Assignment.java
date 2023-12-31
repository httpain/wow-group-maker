package com.httpain.constraints;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
public class Assignment {
    @ValueRangeProvider
    @ProblemFactCollectionProperty
    private List<Character> characters;

    @PlanningEntityCollectionProperty
    private List<Group> groups;

    @PlanningScore
    private HardSoftScore score;

    public Assignment() {
    }

    public Assignment(List<Character> characters, List<Group> groups) {
        this.characters = characters;
        this.groups = groups;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }
}
