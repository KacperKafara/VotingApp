package pl.kafara.voting.vote.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import pl.kafara.voting.model.vote.survey.SurveyKind;

import java.time.LocalDateTime;

public record CreateSurveyRequest(
        @NotBlank
        @Length(min = 3, max = 20)
        String title,
        @NotBlank
        @Length(min = 3, max = 500)
        String description,
        @Future
        @NotNull
        LocalDateTime endDate,
        @NotNull
        SurveyKind surveyKind
) {
}
