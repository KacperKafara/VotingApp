package pl.kafara.voting.vote.dto;

import java.util.List;

public record ActiveVotingsAndSurveysResponse(
        List<VotingWithoutVotesResponse> votingList,
        List<SurveyWithoutVotesResponse> surveys
) {
}
