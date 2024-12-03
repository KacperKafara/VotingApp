package pl.kafara.voting.vote.dto;

public record UserVoteResponse(
        String gender,
        int age,
        String voteResult
) {
}
