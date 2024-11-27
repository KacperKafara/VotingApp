package pl.kafara.voting.vote.mapper;

import pl.kafara.voting.model.vote.VotingOption;
import pl.kafara.voting.vote.dto.VotingOptionResponse;

public class VotingOptionMapper {

    public static VotingOptionResponse votingOptionToVotingOptionResponse(VotingOption votingOption) {
        return new VotingOptionResponse(
                votingOption.getId(),
                votingOption.getOption()
        );
    }

    private VotingOptionMapper() {
    }
}
