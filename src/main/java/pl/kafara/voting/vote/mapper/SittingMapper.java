package pl.kafara.voting.vote.mapper;

import pl.kafara.voting.model.vote.Sitting;
import pl.kafara.voting.vote.dto.SittingResponse;

public class SittingMapper {

    public static SittingResponse sittingToSittingResponse(Sitting sitting) {
        return new SittingResponse(
                sitting.getId(),
                sitting.getNumber(),
                Integer.parseInt(sitting.getTerm().replace("term", ""))
        );
    }

    private SittingMapper() {
    }
}
