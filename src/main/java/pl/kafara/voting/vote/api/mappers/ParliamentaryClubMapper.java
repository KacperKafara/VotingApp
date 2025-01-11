package pl.kafara.voting.vote.api.mappers;

import pl.kafara.voting.model.vote.ParliamentaryClub;
import pl.kafara.voting.vote.api.model.ParliamentaryClubAPI;

public class ParliamentaryClubMapper {

    public static ParliamentaryClub mapToParliamentaryClub(ParliamentaryClubAPI api, String term) {
        return new ParliamentaryClub(
                api.getId(),
                term,
                api.getEmail(),
                api.getFax(),
                api.getMembersCount(),
                api.getName(),
                api.getPhone()
        );
    }

    private ParliamentaryClubMapper() {
    }
}
