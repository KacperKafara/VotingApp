package pl.kafara.voting.users.dto;

import java.util.List;

public record VoterRoleRequestListResponse(
        List<VoterRoleRequestResponse> requests,
        int totalPages,
        int pageNumber,
        int pageSize
) {
}
