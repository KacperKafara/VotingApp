package pl.kafara.voting.users.dto;

import java.util.StringJoiner;

public record LoginResponse(String token) {
    @Override
    public String
    toString() {
        return new StringJoiner(", ", LoginResponse.class.getSimpleName() + "[", "]")
                .add("token='**********'")
                .toString();
    }
}
