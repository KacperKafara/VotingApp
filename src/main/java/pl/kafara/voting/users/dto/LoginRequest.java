package pl.kafara.voting.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.util.StringJoiner;

public record LoginRequest(
        @NotBlank
        @Length(min = 3)
        String username,
        @NotBlank
        @Length(min = 8)
        String password,
        @Pattern(regexp = "pl|en")
        String language
        ) {
    @Override
    public String toString() {
        return new StringJoiner(", ", LoginRequest.class.getSimpleName() + "[", "]")
                .add("username='" + username + "'")
                .add("password='********'")
                .toString();
    }
}
