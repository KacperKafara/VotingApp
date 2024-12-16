package pl.kafara.voting.users.dto.OAuth.google;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record FillDataDTO (
        @NotBlank
        String jwtToken,
        @NotBlank
        String username,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String phoneNumber,
        @NotNull
        LocalDateTime birthDate,
        @Min(0)
        @Max(2)
        int gender
) {
}
