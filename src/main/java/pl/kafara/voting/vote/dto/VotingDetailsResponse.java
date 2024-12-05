package pl.kafara.voting.vote.dto;

import pl.kafara.voting.model.vote.Print;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record VotingDetailsResponse(
        UUID id,
        String sittingTitle,
        int sittingDay,
        LocalDateTime date,
        List<VotingOptionResponse> votingOptions,
        LocalDateTime endDate,
        String title,
        String description,
        String topic,
        List<Print> prints
) {
}
