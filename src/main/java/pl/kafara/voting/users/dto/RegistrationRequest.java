package pl.kafara.voting.users.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public record RegistrationRequest(
        @NotBlank
        String username,
        @NotBlank
        @Size(min = 8)
        String password,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$")
        String phoneNumber,
        @NotNull
        @Past
        LocalDateTime birthDate,
        @Min(0)
        @Max(2)
        int gender,
        @NotBlank(message = "Language name cannot be blank.")
        @Pattern(regexp = "^(en|pl)$", message = "Language name must be 'en' or 'pl'.")
        String language
) {
    @Override
    public String toString() {
        return new StringJoiner(", ", RegistrationRequest.class.getSimpleName() + "[", "]")
                .add("username='" + username + "'")
                .add("password='********'")
                .add("email='" + email + "'")
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("phoneNumber='" + phoneNumber + "'")
                .add("birthDate=" + birthDate)
                .add("language='" + language + "'")
                .toString();
    }
}
