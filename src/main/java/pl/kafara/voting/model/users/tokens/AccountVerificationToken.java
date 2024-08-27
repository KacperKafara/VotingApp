package pl.kafara.voting.model.users.tokens;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import pl.kafara.voting.model.users.User;

import java.time.Instant;

@Entity
@NoArgsConstructor
@DiscriminatorValue("VERIFICATION_TOKEN")
public class AccountVerificationToken extends SafeToken {
    public static int EXPIRATION_TIME = 24 * 60;

    public AccountVerificationToken(String token, Instant expirationDate, User user) {
        super(token, expirationDate, user);
    }
}
