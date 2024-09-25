package pl.kafara.voting.users.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.StringJoiner;

public record ChangePasswordRequest(
        @NotBlank
        @Length(min = 8, max = 50)
        String oldPassword,
        @NotBlank
        @Length(min = 8, max = 50)
        String newPassword
) {
    @Override
    public String toString() {
        return new StringJoiner(", ", ChangePasswordRequest.class.getSimpleName() + "[", "]")
                .add("oldPassword='********'")
                .add("newPassword='********'")
                .toString();
    }
}
