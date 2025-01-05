package pl.kafara.voting.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.StringJoiner;

public record LoginRequest(
        @NotBlank
        @Size(min = 3)
        String username,
        @NotBlank
        @Size(min = 8)
        String password,
        @NotBlank
        @Pattern(regexp = "pl|en")
        String language
        ) {
    @Override
    public String toString() {
        return new StringJoiner(", ", LoginRequest.class.getSimpleName() + "[", "]")
                .add("username='" + username + "'")
                .add("password='********'")
                .add("language='" + language + "'")
                .toString();
    }
}
