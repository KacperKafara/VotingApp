package pl.kafara.voting.vote.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record CreateUserVoteRequest(
        @NotBlank
        @Length(min = 6, max = 6)
        @Pattern(regexp = "\\d{6}")
        String totp,
        UUID surveyId,
        String voteResult
) {
}
