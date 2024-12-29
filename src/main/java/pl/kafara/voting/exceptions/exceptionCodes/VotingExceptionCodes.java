package pl.kafara.voting.exceptions.exceptionCodes;

public class VotingExceptionCodes {
    public static final String VOTING_NOT_FOUND = "VOTING_NOT_FOUND";
    public static final String SITTING_NOT_FOUND = "SITTING_NOT_FOUND";
    public static final String VOTING_NOT_ACTIVE = "VOTING_NOT_ACTIVE";
    public static final String VOTING_ON_LIST = "VOTING_ON_LIST";
    public static final String VOTING_NOT_ON_LIST = "VOTING_NOT_ON_LIST";
    public static final String OPTIMISTIC_LOCK_EXCEPTION = "OPTIMISTIC_LOCK_EXCEPTION";
    public static final String VOTING_ALREADY_ACTIVE = "VOTING_ALREADY_ACTIVE";

    private VotingExceptionCodes() {
    }
}
