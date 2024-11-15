package pl.kafara.voting.vote.mapper;

import pl.kafara.voting.model.vote.Voting;
import pl.kafara.voting.vote.dto.VoteResponse;
import pl.kafara.voting.vote.dto.VotingResponse;
import pl.kafara.voting.vote.dto.VotingWithoutVotesResponse;

import java.util.List;

public class VotingMapper {

    public static VotingResponse votingToVotingResponse(Voting voting) {
        List<VoteResponse> votes = voting.getVotes().stream()
                .map(VoteMapper::voteToVoteResponse)
                .toList();

        return new VotingResponse(
                voting.getId(),
                voting.getTitle(),
                voting.getDescription(),
                voting.getTopic(),
                voting.getDate(),
                voting.getKind(),
                votes
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
