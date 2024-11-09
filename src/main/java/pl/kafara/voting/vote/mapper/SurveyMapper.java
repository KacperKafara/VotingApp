package pl.kafara.voting.vote.mapper;

import pl.kafara.voting.model.vote.survey.Survey;
import pl.kafara.voting.model.vote.survey.UserVote;
import pl.kafara.voting.model.vote.survey.UserVoteOtherSurvey;
import pl.kafara.voting.model.vote.survey.UserVoteParliamentaryClub;
import pl.kafara.voting.vote.dto.CreateSurveyRequest;
import pl.kafara.voting.vote.dto.SurveyResponse;
import pl.kafara.voting.vote.dto.UserVoteResponse;

import java.util.List;

public class SurveyMapper {

    public static UserVoteResponse userVoteToUserVoteResponse(UserVote userVote) {
        String result;
        if(userVote instanceof UserVoteOtherSurvey) {
            result = ((UserVoteOtherSurvey) userVote).getResult().name();
        } else {
            result = ((UserVoteParliamentaryClub) userVote).getParliamentaryClub().getId() + " - " + ((UserVoteParliamentaryClub) userVote).getParliamentaryClub().getName();
        }

        return new UserVoteResponse(
                userVote.getUser().getGender().getName().name(),
                userVote.getUser().getBirthDate(),
                result
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

    public static SurveyResponse surveyToSurveyResponse(Survey survey) {
        List<UserVoteResponse> votes = survey.getUserVotes().stream()
                .map(SurveyMapper::userVoteToUserVoteResponse)
                .toList();

        return new SurveyResponse(
                survey.getId(),
                survey.getTitle(),
                survey.getDescription(),
                survey.getEndDate(),
                survey.getCreatedAt(),
                survey.getSurveyKind().name(),
                votes
        );
    }

    private SurveyMapper() {
    }
}
