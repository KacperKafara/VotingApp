package pl.kafara.voting.vote.dto;

import java.util.UUID;

public record SittingResponse(
        UUID id,
        Long number,
        int term
) {
}
