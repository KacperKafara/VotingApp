package pl.kafara.voting.model.vote;

public enum VoteResult {
    YES, NO, ABSTAIN, ABSENT, VOTE_VALID;

    public static VoteResult fromString(String value) {
        if (value == null || value.isEmpty())
            return null;

        for (VoteResult voteResult : VoteResult.values()) {
            if(voteResult.name().equals(value)) {
                return voteResult;
            }
        }

        throw new IllegalArgumentException("No VoteResult with value: " + value);
    }
}
