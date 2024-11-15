package pl.kafara.voting.vote.dto;

import java.util.List;

public record VotingListResponse (
        List<VotingWithoutVotesResponse> votingList,
        List<Long> sittings,
        int totalPages,
        int pageNumber,
        int pageSize
) {
}
