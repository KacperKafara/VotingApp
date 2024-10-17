package pl.kafara.voting.vote.api.mappers;

import pl.kafara.voting.vote.api.model.EnvoyAPI;
import pl.kafara.voting.model.vote.Envoy;
import pl.kafara.voting.model.vote.ParliamentaryClub;

public class EnvoyMapper {
    public static Envoy update(EnvoyAPI envoyAPI, ParliamentaryClub parliamentaryClub) {
        Envoy envoyToUpdate = new Envoy();

        envoyToUpdate.setId(envoyAPI.getId());
        envoyToUpdate.setClub(parliamentaryClub);
        envoyToUpdate.setFirstName(envoyAPI.getFirstName());
        envoyToUpdate.setLastName(envoyAPI.getLastName());
        envoyToUpdate.setNumberOfVotes(envoyAPI.getNumberOfVotes());
        envoyToUpdate.setEmail(envoyAPI.getEmail());
        envoyToUpdate.setDistrictName(envoyAPI.getDistrictName());
        envoyToUpdate.setActive(envoyAPI.isActive());

        return envoyToUpdate;
    }
}
