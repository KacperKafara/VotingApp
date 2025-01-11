package pl.kafara.voting.vote.dto;

import java.util.UUID;

public record ParliamentaryClubResponse(
        UUID id,
        String shortName
) {
}
