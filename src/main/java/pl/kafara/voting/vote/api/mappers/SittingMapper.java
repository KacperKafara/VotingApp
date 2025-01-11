package pl.kafara.voting.vote.api.mappers;

import pl.kafara.voting.model.vote.Sitting;
import pl.kafara.voting.vote.api.model.SittingAPI;

public class SittingMapper {

    public static Sitting toEntity(SittingAPI sittingAPI, String term) {
        return new Sitting(sittingAPI.getNumber(), sittingAPI.getTitle(), term);
    }

    public SittingMapper() {
    }
}
