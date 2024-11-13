package pl.kafara.voting.vote.mapper;

import pl.kafara.voting.model.vote.Voting;
import pl.kafara.voting.vote.dto.VoteResponse;
import pl.kafara.voting.vote.dto.VotingResponse;

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
                voting.getYes(),
                voting.getNo(),
                voting.getAbstain(),
                voting.getNotParticipating(),
                voting.getDate(),
                voting.getKind(),
                votes
        );
    }

    private VotingMapper() {
    }
}
