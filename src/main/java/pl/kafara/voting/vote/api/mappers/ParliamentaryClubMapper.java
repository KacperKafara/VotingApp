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

    public static ParliamentaryClub update(ParliamentaryClub parliamentaryClub, ParliamentaryClubAPI api) {
        parliamentaryClub.setEmail(api.getEmail());
        parliamentaryClub.setFax(api.getFax());
        parliamentaryClub.setMembersCount(api.getMembersCount());
        parliamentaryClub.setName(api.getName());
        parliamentaryClub.setPhone(api.getPhone());

        return parliamentaryClub;
    }

    private ParliamentaryClubMapper() {
    }
}
