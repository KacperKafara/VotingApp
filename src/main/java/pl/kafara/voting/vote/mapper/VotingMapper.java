package pl.kafara.voting.vote.mapper;

import pl.kafara.voting.model.vote.Voting;
import pl.kafara.voting.model.vote.userVotes.UserVoteOnList;
import pl.kafara.voting.model.vote.userVotes.UserVoteOther;
import pl.kafara.voting.vote.dto.UserVoteResponse;
import pl.kafara.voting.vote.dto.VoteResponse;
import pl.kafara.voting.vote.dto.VotingResponse;
import pl.kafara.voting.vote.dto.VotingWithoutVotesResponse;

import java.util.List;
import java.util.UUID;

public class VotingMapper {

    public static UserVoteResponse userVoteToUserVoteResponse(UserVoteOnList userVoteOnList) {
        return new UserVoteResponse(
                userVoteOnList.getGender().getName().name(),
                userVoteOnList.getAge(),
                userVoteOnList.getVotingOption().getOption()
        );
    }

    public static UserVoteResponse userVoteToUserVoteResponse(UserVoteOther userVoteOther) {
        return new UserVoteResponse(
                userVoteOther.getGender().getName().name(),
                userVoteOther.getAge(),
                userVoteOther.getVoteResult().name()
        );
    }

    public static VotingResponse votingToVotingResponse(Voting voting, UUID userId) {
        List<VoteResponse> votes = voting.getVotes().stream()
                .map(VoteMapper::voteToVoteResponse)
                .toList();

        boolean userVoted = voting.getUserVotes().stream()
                .anyMatch(vote -> vote.getUser().getId().equals(userId));

        List<UserVoteResponse> userVotes = voting.getUserVotes().stream()
                .map(userVote -> {
                    if (userVote instanceof UserVoteOnList) {
                        return userVoteToUserVoteResponse((UserVoteOnList) userVote);
                    } else {
                        return userVoteToUserVoteResponse((UserVoteOther) userVote);
                    }
                })
                .toList();

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
                userVoted,
                userVotes
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
