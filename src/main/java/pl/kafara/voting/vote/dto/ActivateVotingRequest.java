package pl.kafara.voting.vote.dto;

import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public record ActivateVotingRequest(
        @Future
        LocalDateTime endDate
) {
}
