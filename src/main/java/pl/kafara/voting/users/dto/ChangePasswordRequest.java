package pl.kafara.voting.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.StringJoiner;

public record ChangePasswordRequest(
        @NotBlank
        @Size(min = 8, max = 50)
        String oldPassword,
        @NotBlank
        @Size(min = 8, max = 50)
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
