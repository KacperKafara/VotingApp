package pl.kafara.voting.model.users;

public enum UserRoleEnum {
    ADMINISTRATOR, MODERATOR, USER;

    public static UserRoleEnum fromString(String value) {
        for(UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
            if (userRoleEnum.name().equals(value)) {
                return userRoleEnum;
            }
        }
        throw new IllegalArgumentException("No UserRoleEnum with value " + value);
    }
}
