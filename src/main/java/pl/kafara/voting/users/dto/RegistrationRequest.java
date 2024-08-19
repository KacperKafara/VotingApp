package pl.kafara.voting.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public record RegistrationRequest(
        @NotBlank
        String username,
        @NotBlank
        @Length(min = 8)
        String password,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        @Length(min = 9, max = 9)
        String phoneNumber,
        @NotNull
        LocalDateTime birthDate
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
                .toString();
    }
}
