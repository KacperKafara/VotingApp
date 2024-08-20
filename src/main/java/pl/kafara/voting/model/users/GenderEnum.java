package pl.kafara.voting.model.users;

import lombok.Getter;

@Getter
public enum GenderEnum {
    MALE(1), FEMALE(2), OTHER(0);
    private final int value;

    GenderEnum(int value) {
        this.value = value;
    }

    public static GenderEnum fromInt(int value) {
        for(GenderEnum genderEnum : GenderEnum.values()) {
            if (genderEnum.value == value) {
                return genderEnum;
            }
        }
        throw new IllegalArgumentException("No GenderEnum with value " + value);
    }
}
