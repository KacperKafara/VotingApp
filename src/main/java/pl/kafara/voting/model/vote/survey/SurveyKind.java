package pl.kafara.voting.model.vote.survey;

public enum SurveyKind {
    PARLIAMENTARY_CLUB, OTHER;

    public static SurveyKind fromString(String value) {
        if (value == null || value.isEmpty())
            return null;

        for (SurveyKind voteResult : SurveyKind.values()) {
            if(voteResult.name().equals(value)) {
                return voteResult;
            }
        }

        throw new IllegalArgumentException("No SurveyKind with value: " + value);
    }
}
