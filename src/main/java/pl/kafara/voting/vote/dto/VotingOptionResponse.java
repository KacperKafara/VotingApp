package pl.kafara.voting.vote.dto;

import java.util.UUID;

public record VotingOptionResponse (
        UUID id,
        String option
) {
}
