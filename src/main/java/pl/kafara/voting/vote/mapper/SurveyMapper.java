package pl.kafara.voting.vote.mapper;

import pl.kafara.voting.model.vote.survey.Survey;
import pl.kafara.voting.model.vote.survey.SurveyKind;
import pl.kafara.voting.vote.dto.CreateSurveyRequest;
import pl.kafara.voting.vote.dto.SurveyResponse;

public class SurveyMapper {

    public static Survey createSurveyRequestToSurvey(CreateSurveyRequest request) {
        Survey survey = new Survey();
        survey.setTitle(request.title());
        survey.setDescription(request.description());
        survey.setEndDate(request.endDate());
        survey.setSurveyKind(request.surveyKind());
        return survey;
    }

    public static SurveyResponse surveyToSurveyResponse(Survey survey) {
        return new SurveyResponse(
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
