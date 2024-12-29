package pl.kafara.voting.vote.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ActivateVotingRequest(
        @Future
        @NotNull
        LocalDateTime endDate
) {
}
