package pl.kafara.voting.vote.dto;

import java.time.LocalDateTime;

public record UserVoteResponse(
        String gender,
        LocalDateTime birthDate,
        String voteResult
) {
}
