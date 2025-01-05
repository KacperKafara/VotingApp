package pl.kafara.voting.vote.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.kafara.voting.model.vote.survey.SurveyKind;

import java.time.LocalDateTime;

public record CreateSurveyRequest(
        @NotBlank
        @Size(min = 3, max = 20)
        String title,
        @NotBlank
        @Size(min = 3, max = 500)
        String description,
        @Future
        @NotNull
        LocalDateTime endDate,
        @NotNull
        SurveyKind surveyKind
) {
}
