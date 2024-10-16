package pl.kafara.voting.model.vote;

public enum VotingKind {
    ELECTRONIC, TRADITIONAL, ON_LIST;

    public static VotingKind fromString(String value) {
        if(value == null || value.isEmpty())
            return null;
        for(VotingKind votingKind : VotingKind.values()) {
            if (votingKind.name().equals(value)) {
                return votingKind;
            }
        }
        throw new IllegalArgumentException("No VotingKind with value " + value);
    }
}
