package pl.kafara.voting.vote.mapper;

import pl.kafara.voting.model.vote.Envoy;
import pl.kafara.voting.vote.dto.EnvoyResponse;

public class EnvoyMapper {
    public static EnvoyResponse envoyToEnvoyResponse(Envoy envoy) {
        return new EnvoyResponse(
                envoy.getFirstName(),
                envoy.getLastName(),
                envoy.getClub().getShortName()
        );
    }

    private EnvoyMapper() {
    }
}
