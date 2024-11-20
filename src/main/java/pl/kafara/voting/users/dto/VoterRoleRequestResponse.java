package pl.kafara.voting.users.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record VoterRoleRequestResponse (
        UUID id,
        UserResponse user,
        LocalDateTime requestDate
) {
}
