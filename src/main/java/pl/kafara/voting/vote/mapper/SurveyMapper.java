package pl.kafara.voting.vote.mapper;

import pl.kafara.voting.model.vote.survey.Survey;
import pl.kafara.voting.model.vote.survey.UserVoteSurvey;
import pl.kafara.voting.model.vote.survey.UserVoteOtherSurvey;
import pl.kafara.voting.model.vote.survey.UserVoteParliamentaryClub;
import pl.kafara.voting.vote.dto.CreateSurveyRequest;
import pl.kafara.voting.vote.dto.SurveyResponse;
import pl.kafara.voting.vote.dto.SurveyWithoutVotesResponse;
import pl.kafara.voting.vote.dto.UserVoteResponse;

import java.util.List;
import java.util.UUID;

public class SurveyMapper {

    public static UserVoteResponse userVoteToUserVoteResponse(UserVoteSurvey userVote) {
        String result;
        String userParliamentaryClub = null;
        if(userVote instanceof UserVoteOtherSurvey) {
            result = ((UserVoteOtherSurvey) userVote).getResult().name();
            userParliamentaryClub = ((UserVoteOtherSurvey) userVote).getParliamentaryClub().getShortName();
        } else {
            result = ((UserVoteParliamentaryClub) userVote).getParliamentaryClub().getShortName();
        }

        return new UserVoteResponse(
                userVote.getGender().getName().name(),
                userVote.getAge(),
                result,
                userParliamentaryClub
        );
    }

    public static Survey createSurveyRequestToSurvey(CreateSurveyRequest request) {
        Survey survey = new Survey();
        survey.setTitle(request.title());
        survey.setDescription(request.description());
        survey.setEndDate(request.endDate());
        survey.setSurveyKind(request.surveyKind());
        return survey;
    }

    public static SurveyResponse surveyToSurveyResponse(Survey survey, UUID userId) {
        List<UserVoteResponse> votes = survey.getUserVotes().stream()
                .map(SurveyMapper::userVoteToUserVoteResponse)
                .toList();

        boolean userVoted = userId != null &&
                survey.getUserVotes().stream()
                        .anyMatch(userVote -> userVote.getUser().getId().equals(userId));

        return new SurveyResponse(
                survey.getId(),
                survey.getTitle(),
                survey.getDescription(),
                survey.getEndDate(),
                survey.getCreatedAt(),
                survey.getSurveyKind().name(),
                votes,
                userVoted
        );
    }

    public static SurveyWithoutVotesResponse surveyToSurveyWithoutVotesResponse(Survey survey) {
        return new SurveyWithoutVotesResponse(
                survey.getId(),
                survey.getTitle(),
                survey.getDescription(),
                survey.getEndDate(),
                survey.getCreatedAt(),
                survey.getSurveyKind().name()
        );
    }

    private SurveyMapper() {
    }
}
