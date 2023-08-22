package example.Advanced.Music.app.enums;

import lombok.Getter;

public enum RoleEnum {
    ROLE_ADMIN("ROLE_USER"),
    ROLE_USER("ROLE_ADMIN");
    @Getter
    private final String value;
    private RoleEnum(String value) {
        this.value = value;
    }
}
