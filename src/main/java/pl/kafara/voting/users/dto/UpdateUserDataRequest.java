package pl.kafara.voting.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record UpdateUserDataRequest(
        @NotBlank
        @Length(min = 3)
        String username,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String phoneNumber,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Pattern(regexp = "^(MALE|FEMALE|OTHER)$")
        String gender
) {
}
