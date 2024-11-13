package pl.kafara.voting.vote.dto;

public record VoteResponse(
        EnvoyResponse envoy,
        String vote,
        String club,
        String votingOption
) {
}
