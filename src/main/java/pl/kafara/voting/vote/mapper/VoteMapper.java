package pl.kafara.voting.vote.mapper;

import pl.kafara.voting.model.vote.Vote;
import pl.kafara.voting.vote.dto.VoteResponse;

public class VoteMapper {
    public static VoteResponse voteToVoteResponse(Vote vote) {
        String votingOption = vote.getVotingOption() == null ? null : vote.getVotingOption().getOption();

        return new VoteResponse(
                EnvoyMapper.envoyToEnvoyResponse(vote.getEnvoy()),
                vote.getVote().toString(),
                vote.getClub(),
                votingOption
        );
    }

    private VoteMapper() {
    }
}
