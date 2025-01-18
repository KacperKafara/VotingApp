package pl.kafara.voting.users.dto;

import java.util.UUID;

public record UpdateParliamentaryClubRequest(
        UUID parliamentaryClubId
) {
}
