package pl.kafara.voting.model.users;

public enum RoleRequestResolution {
    ACCEPTED,
    REJECTED,
    PENDING;

    public static RoleRequestResolution fromString(String value) {
        if(value == null || value.isEmpty())
            return null;
        for(RoleRequestResolution roleRequestResolution : RoleRequestResolution.values()) {
            if (roleRequestResolution.name().equals(value)) {
                return roleRequestResolution;
            }
        }
        throw new IllegalArgumentException("No RoleRequestResolution with value " + value);
    }
}
