package pl.kafara.voting.users.dto;

import pl.kafara.voting.model.users.UserRoleEnum;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDateTime birthDate,
        String username,
        String email,
        String gender,
        Set<UserRoleEnum> roles,
        boolean blocked,
        boolean verified,
        LocalDateTime lastLogin,
        LocalDateTime lastFailedLogin,
        boolean activeRoleRequest
) {
}
