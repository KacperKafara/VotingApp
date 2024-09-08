package pl.kafara.voting.users.dto;

import org.hibernate.validator.constraints.Length;

public record ResetPasswordFormRequest(
        @Length(min = 8, max = 50)
        String password
) {
}
