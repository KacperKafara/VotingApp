package pl.kafara.voting.api.mappers;

import pl.kafara.voting.api.model.VotingAPI;
import pl.kafara.voting.model.vote.Sitting;
import pl.kafara.voting.model.vote.Voting;
import pl.kafara.voting.model.vote.VotingKind;

public class VotingMapper {
    public static Voting update(VotingAPI votingAPI, Sitting sitting) {
        Voting votingToUpdate = new Voting();
        votingToUpdate.setVotingNumber(votingAPI.getVotingNumber());
        votingToUpdate.setSitting(sitting);
        votingToUpdate.setTerm(votingAPI.getTerm());
        votingToUpdate.setSittingDay(votingAPI.getSittingDay());
        votingToUpdate.setYes(votingAPI.getYes());
        votingToUpdate.setNo(votingAPI.getNo());
        votingToUpdate.setAbstain(votingAPI.getAbstain());
        votingToUpdate.setNotParticipating(votingAPI.getNotParticipating());
        votingToUpdate.setTotalVoted(votingAPI.getTotalVoted());
        votingToUpdate.setDate(votingAPI.getDate());
        votingToUpdate.setTitle(votingAPI.getTitle());
        votingToUpdate.setDescription(votingAPI.getDescription());
        votingToUpdate.setTopic(votingAPI.getTopic());
        votingToUpdate.setKind(VotingKind.fromString(votingAPI.getKind()));

        return votingToUpdate;
    }
}
