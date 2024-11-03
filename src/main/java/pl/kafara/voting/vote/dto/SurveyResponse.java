package pl.kafara.voting.vote.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SurveyResponse(
        UUID id,
        String title,
        String description,
        LocalDateTime endDate
) {
}
