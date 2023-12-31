package com.httpain.constraints;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@PlanningEntity
public class Group {

    @PlanningId
    private int groupNumber;

    @PlanningVariable(nullable = true)
    private Character tank;

    @PlanningVariable(nullable = true)
    private Character healer;

    @PlanningVariable(nullable = true)
    private Character dps1;

    @PlanningVariable(nullable = true)
    private Character dps2;

    @PlanningVariable(nullable = true)
    private Character dps3;

    public Group(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public Group() {
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public Character getTank() {
        return tank;
    }

    public Character getHealer() {
        return healer;
    }

    public List<Character> dps() {
        return Arrays.asList(dps1, dps2, dps3);
    }

    public List<Character> members() {
        return Arrays.asList(tank, healer, dps1, dps2, dps3);
    }

    public void setTank(Character tank) {
        this.tank = tank;
    }

    public void setHealer(Character healer) {
        this.healer = healer;
    }

    public void setDps1(Character dps1) {
        this.dps1 = dps1;
    }

    public void setDps2(Character dps2) {
        this.dps2 = dps2;
    }

    public void setDps3(Character dps3) {
        this.dps3 = dps3;
    }

    @Override
    public String toString() {
        return members().stream().map( member -> {if (member == null) {return "null";} else {return member.name;} }).collect(Collectors.joining("/"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(groupNumber, group.groupNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupNumber);
    }
}
