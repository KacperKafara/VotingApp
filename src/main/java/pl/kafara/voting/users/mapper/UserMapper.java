package pl.kafara.voting.users.mapper;

import pl.kafara.voting.model.users.Role;
import pl.kafara.voting.model.users.RoleRequestResolution;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.users.UserRoleEnum;
import pl.kafara.voting.users.dto.UserResponse;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {
    private UserMapper() {
    }

    public static UserResponse mapToUserResponse(User user) {
        Set<UserRoleEnum> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        boolean activeRoleRequest = user.getVoterRoleRequest() != null && (user.getVoterRoleRequest().getResolution().equals(RoleRequestResolution.PENDING) || user.getVoterRoleRequest().getResolution().equals(RoleRequestResolution.ACCEPTED));
        boolean active2fa = user.getAuthorisationTotpSecret() != null;

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getBirthDate(),
                user.getUsername(),
                user.getEmail(),
                user.getGender().getName().toString(),
                roles,
                user.isBlocked(),
                user.isVerified(),
                user.getLastLogin(),
                user.getLastFailedLogin(),
                activeRoleRequest,
                active2fa
        );
    }
}
