package pl.kafara.voting.exceptions.exceptionCodes;

public class SurveyExceptionCodes {
    public static final String SURVEY_NOT_FOUND = "SURVEY_NOT_FOUND";
    public static final String PARLIAMENTARY_CLUB_NOT_FOUND = "PARLIAMENTARY_CLUB_NOT_FOUND";
    public static final String INVALID_TOTP = "INVALID_TOTP";
    public static final String VOTING_OPTION_NOT_FOUND = "VOTING_OPTION_NOT_FOUND";
    public static final String SURVEY_NOT_ACTIVE = "SURVEY_NOT_ACTIVE";
    public static final String SURVEY_KIND_NOT_PARLIAMENTARY_CLUB = "SURVEY_KIND_NOT_PARLIAMENTARY_CLUB";
    public static final String SURVEY_KIND_NOT_OTHER = "SURVEY_KIND_NOT_OTHER";

    private SurveyExceptionCodes() {
    }
}
