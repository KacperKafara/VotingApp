package pl.kafara.voting.vote.api.mappers;

import pl.kafara.voting.model.vote.VotingOption;
import pl.kafara.voting.vote.api.model.VotingAPI;
import pl.kafara.voting.model.vote.Sitting;
import pl.kafara.voting.model.vote.Voting;
import pl.kafara.voting.model.vote.VotingKind;

import java.util.List;

public class VotingMapper {
    public static Voting update(VotingAPI votingAPI, Sitting sitting) {
        Voting votingToUpdate = new Voting();
        votingToUpdate.setVotingNumber(votingAPI.getVotingNumber());
        votingToUpdate.setSitting(sitting);
        votingToUpdate.setSittingDay(votingAPI.getSittingDay());
        votingToUpdate.setDate(votingAPI.getDate());
        votingToUpdate.setTitle(votingAPI.getTitle());
        votingToUpdate.setDescription(votingAPI.getDescription());
        votingToUpdate.setTopic(votingAPI.getTopic());
        votingToUpdate.setKind(VotingKind.fromString(votingAPI.getKind()));

        return votingToUpdate;
    }

    public static Voting update(VotingAPI votingAPI, Sitting sitting, List<VotingOption> votingOptions) {
        Voting votingToUpdate = VotingMapper.update(votingAPI, sitting);
        votingToUpdate.setVotingOptions(votingOptions);
        for(VotingOption vo : votingOptions)
            vo.setVoting(votingToUpdate);

        return votingToUpdate;
    }
}
