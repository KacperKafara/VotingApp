package pl.kafara.voting.vote.api.mappers;

import pl.kafara.voting.model.vote.*;
import pl.kafara.voting.vote.api.model.VoteAPI;

public class VoteMapper {
    public static Vote update(VoteAPI voteAPI, Voting voting, Envoy envoy) {
        Vote voteToUpdate = new Vote();
        voteToUpdate.setEnvoy(envoy);
        voteToUpdate.setClub(voteAPI.getClub());
        voteToUpdate.setVote(VoteResult.fromString(voteAPI.getVote()));
        voteToUpdate.setVoting(voting);
        return voteToUpdate;
    }

    public static Vote update(VoteAPI voteAPI, Voting voting, Envoy envoy, VotingOption votingOption) {
        Vote voteToUpdate = update(voteAPI, voting, envoy);
        voteToUpdate.setVotingOption(votingOption);
        return voteToUpdate;
    }
}
