package pl.kafara.voting.users.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record TotpLoginRequest(
        @NotBlank
        @Length(min = 3)
        String username,
        @NotBlank
        @Length(min = 6, max = 6)
        String totp
) {
}
