package pl.kafara.voting.vote.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.util.StringJoiner;

public record CreateUserVoteRequest(
        @NotBlank
        @Length(min = 6, max = 6)
        @Pattern(regexp = "\\d{6}")
        String totp,
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
