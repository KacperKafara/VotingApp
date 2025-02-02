package pl.kafara.voting.vote.mapper;

import pl.kafara.voting.model.vote.Voting;
import pl.kafara.voting.model.vote.userVotes.UserVoteOnList;
import pl.kafara.voting.model.vote.userVotes.UserVoteOther;
import pl.kafara.voting.vote.dto.*;

import java.util.List;
import java.util.UUID;

public class VotingMapper {

    public static UserVoteResponse userVoteToUserVoteResponse(UserVoteOnList userVoteOnList) {
        return new UserVoteResponse(
                userVoteOnList.getGender().getName().name(),
                userVoteOnList.getAge(),
                userVoteOnList.getVotingOption().getOption(),
                userVoteOnList.getParliamentaryClub().getShortName()
        );
    }

    public static UserVoteResponse userVoteToUserVoteResponse(UserVoteOther userVoteOther) {
        return new UserVoteResponse(
                userVoteOther.getGender().getName().name(),
                userVoteOther.getAge(),
                userVoteOther.getVoteResult().name(),
                userVoteOther.getParliamentaryClub().getShortName()
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

    public static VotingDetailsResponse votingToVotingDetailsResponse(Voting voting) {
        List<VotingOptionResponse> votingOptions = voting.getVotingOptions().stream()
                .map(VotingOptionMapper::votingOptionToVotingOptionResponse)
                .toList();

        return new VotingDetailsResponse(
                voting.getId(),
                voting.getSitting().getTitle(),
                voting.getSittingDay(),
                voting.getDate(),
                votingOptions,
                voting.getEndDate(),
                voting.getTitle(),
                voting.getDescription(),
                voting.getTopic(),
                voting.getPrints()
        );
    }
}
