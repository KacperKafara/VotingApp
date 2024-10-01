package pl.kafara.voting.users.dto;

import java.util.List;

public record UsersResponse(
        List<UserResponse> users,
        int totalPages,
        int pageNumber,
        int pageSize
) {
}
