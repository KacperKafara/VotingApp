package pl.kafara.voting.users.dto;

import jakarta.validation.constraints.Size;

import java.util.StringJoiner;

public record ResetPasswordFormRequest(
        @Size(min = 8, max = 50)
        String password
) {
        @Override
        public String toString() {
                return new StringJoiner(", ", ResetPasswordFormRequest.class.getSimpleName() + "[", "]")
                        .add("password='********'")
                        .toString();
        }
}
