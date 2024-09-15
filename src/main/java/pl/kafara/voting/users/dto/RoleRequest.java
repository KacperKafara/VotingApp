package pl.kafara.voting.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record RoleRequest(
        @NotNull
        UUID userId,
        @NotBlank
        @Pattern(regexp = "ADMINISTRATOR|USER|MODERATOR")
        String role
) {
}
