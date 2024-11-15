package pl.kafara.voting.vote.dto;

import pl.kafara.voting.model.vote.VotingKind;

import java.time.LocalDateTime;
import java.util.UUID;

public record VotingWithoutVotesResponse(
        UUID id,
        String title,
        String description,
        String topic,
        LocalDateTime date,
        VotingKind kind
) {
}
