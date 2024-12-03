package pl.kafara.voting.vote.dto;

import java.util.List;

public record OwnVotesListResponse(
        List<OwnVoteResponse> ownVotes,
        int totalPages,
        int pageNumber,
        int pageSize
) {
}
