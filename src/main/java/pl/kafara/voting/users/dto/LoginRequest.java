package pl.kafara.voting.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.StringJoiner;

@AllArgsConstructor
@Getter
public class LoginRequest {
    private final String username;
    private final String password;

    @Override
    public String toString() {
        return new StringJoiner(", ", LoginRequest.class.getSimpleName() + "[", "]")
                .add("username='" + username + "'")
                .add("password='********'")
                .toString();
    }
}
