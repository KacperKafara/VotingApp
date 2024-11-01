package pl.kafara.voting.model.vote.survey;

import pl.kafara.voting.model.vote.VoteResult;

public enum UserVoteResult {
    DEFINITELY_YES, YES, I_DONT_KNOW, NO, DEFINITELY_NO;

    public static UserVoteResult fromString(String value) {
        if (value == null || value.isEmpty())
            return null;

        for (UserVoteResult voteResult : UserVoteResult.values()) {
            if(voteResult.name().equals(value)) {
                return voteResult;
            }
        }

        throw new IllegalArgumentException("No VoteResult with value: " + value);
    }
}
