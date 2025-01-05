package pl.kafara.voting.vote.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.StringJoiner;

public record CreateUserVoteRequest(
        @NotBlank
        @Size(min = 6, max = 6)
        @Pattern(regexp = "\\d{6}")
        String totp,
        @NotBlank
        String voteResult
) {
        @Override
        public String toString() {
                return new StringJoiner(", ", CreateUserVoteRequest.class.getSimpleName() + "[", "]")
                        .add("totp='******'")
                        .add("voteResult='" + voteResult + "'")
                        .toString();
        }
}
