package com.httpain.constraints;

import java.util.Objects;

public class Character {
    String name;
    Role role;
    int keyLevel;

    public Character(String name, Role role, int keyLevel) {
        this.name = name;
        this.role = role;
        this.keyLevel = keyLevel;
    }

    public Character() {
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public int getKeyLevel() {
        return keyLevel;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return keyLevel == character.keyLevel && Objects.equals(name, character.name) && role == character.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, role, keyLevel);
    }
}
