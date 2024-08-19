package pl.kafara.voting.users.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.StringJoiner;

public record LoginRequest(
        @NotBlank
        String username,
        @NotBlank
        @Length(min = 8)
        String password) {
    @Override
    public String toString() {
        return new StringJoiner(", ", LoginRequest.class.getSimpleName() + "[", "]")
                .add("username='" + username + "'")
                .add("password='********'")
                .toString();
    }
}
