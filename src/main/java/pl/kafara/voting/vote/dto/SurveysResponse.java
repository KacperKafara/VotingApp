package pl.kafara.voting.vote.dto;

import java.util.List;

public record SurveysResponse(
        List<SurveyResponse> surveys,
        int totalPages,
        int pageNumber,
        int pageSize
) {
}
