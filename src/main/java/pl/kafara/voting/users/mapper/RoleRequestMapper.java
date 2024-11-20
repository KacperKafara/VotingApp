package pl.kafara.voting.users.mapper;

import pl.kafara.voting.model.users.VoterRoleRequest;
import pl.kafara.voting.users.dto.VoterRoleRequestResponse;

public class RoleRequestMapper {
    public static VoterRoleRequestResponse toVoterRoleRequestResponse(VoterRoleRequest roleRequest) {
        return new VoterRoleRequestResponse(
                roleRequest.getId(),
                UserMapper.mapToUserResponse(roleRequest.getUser()),
                roleRequest.getRequestDate()
        );
    }

    private RoleRequestMapper() {
    }
}
