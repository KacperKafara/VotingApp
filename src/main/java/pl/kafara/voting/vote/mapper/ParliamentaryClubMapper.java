package pl.kafara.voting.vote.mapper;

import pl.kafara.voting.model.vote.ParliamentaryClub;
import pl.kafara.voting.vote.dto.ParliamentaryClubResponse;

public class ParliamentaryClubMapper {

    public static ParliamentaryClubResponse parliamentaryClubToParliamentaryClubResponse(ParliamentaryClub parliamentaryClub) {
        return new ParliamentaryClubResponse(parliamentaryClub.getId(), parliamentaryClub.getShortName());
    }

    private ParliamentaryClubMapper() {
    }
}
