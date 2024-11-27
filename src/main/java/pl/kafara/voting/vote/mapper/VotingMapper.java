package pl.kafara.voting.vote.mapper;

import pl.kafara.voting.model.vote.Voting;
import pl.kafara.voting.vote.dto.VoteResponse;
import pl.kafara.voting.vote.dto.VotingResponse;
import pl.kafara.voting.vote.dto.VotingWithoutVotesResponse;

import java.util.List;
import java.util.UUID;

public class VotingMapper {

    public static VotingResponse votingToVotingResponse(Voting voting, UUID userId) {
        List<VoteResponse> votes = voting.getVotes().stream()
                .map(VoteMapper::voteToVoteResponse)
                .toList();

        boolean userVoted = voting.getUserVotes().stream()
                .anyMatch(vote -> vote.getUser().getId().equals(userId));

        return new VotingResponse(
                voting.getId(),
                voting.getTitle(),
                voting.getDescription(),
                voting.getTopic(),
                voting.getDate(),
                voting.getEndDate(),
                voting.getKind(),
                votes,
                voting.getPrints(),
                userVoted
        );
    }

    public static VotingWithoutVotesResponse votingToVotingWithoutVotesResponse(Voting voting) {
        return new VotingWithoutVotesResponse(
                voting.getId(),
                voting.getTitle(),
                voting.getDescription(),
                voting.getTopic(),
                voting.getDate(),
                voting.getKind()
        );
    }

    private VotingMapper() {
    }
}
