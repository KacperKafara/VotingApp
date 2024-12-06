package pl.kafara.voting.users.dto;

import jakarta.validation.constraints.NotNull;

public record Change2FaStateRequest(
        @NotNull
        boolean active
) {
}
