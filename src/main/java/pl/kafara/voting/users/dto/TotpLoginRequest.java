package pl.kafara.voting.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TotpLoginRequest(
        @NotBlank
        @Size(min = 3)
        String username,
        @NotBlank
        @Size(min = 6, max = 6)
        String totp
) {
}
