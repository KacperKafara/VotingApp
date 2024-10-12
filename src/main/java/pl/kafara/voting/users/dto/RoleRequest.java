package pl.kafara.voting.users.dto;

import jakarta.validation.constraints.NotEmpty;
import pl.kafara.voting.model.users.UserRoleEnum;

import java.util.Set;

public record RoleRequest(
        @NotEmpty
        Set<UserRoleEnum> roles
) {
}
