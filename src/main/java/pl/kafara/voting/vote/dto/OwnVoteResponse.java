package pl.kafara.voting.vote.dto;

public record OwnVoteResponse (
        String voteResult,
        String votingOrSurveyId,
        String votingOrSurveyTitle
) {
}
