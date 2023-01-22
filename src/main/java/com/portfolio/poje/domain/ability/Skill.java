package com.portfolio.poje.domain.ability;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Skill {
    JAVA("java"), PYTHON("python"), JAVASCRIPT("javascript");

    private final String value;

    Skill(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Skill from(String value) {
        for (Skill type : Skill.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
