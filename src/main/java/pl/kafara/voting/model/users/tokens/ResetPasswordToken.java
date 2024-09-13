package pl.kafara.voting.model.users.tokens;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import pl.kafara.voting.model.users.User;

import java.time.Instant;
import java.util.StringJoiner;

@Entity
@NoArgsConstructor
@DiscriminatorValue("RESET_PASSWORD_TOKEN")
public class ResetPasswordToken extends SafeToken {
    public static final int EXPIRATION_TIME = 15;

    public ResetPasswordToken(String token, Instant expirationDate, User user) {
        super(token, expirationDate, user);
    }
}
