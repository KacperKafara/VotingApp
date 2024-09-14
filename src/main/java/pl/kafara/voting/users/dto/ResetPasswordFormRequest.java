package pl.kafara.voting.users.dto;

import org.hibernate.validator.constraints.Length;

import java.util.StringJoiner;

public record ResetPasswordFormRequest(
        @Length(min = 8, max = 50)
        String password
) {
        @Override
        public String toString() {
                return new StringJoiner(", ", ResetPasswordFormRequest.class.getSimpleName() + "[", "]")
                        .add("password='********'")
                        .toString();
        }
}
