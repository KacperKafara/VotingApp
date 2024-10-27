package pl.kafara.voting.model.users.tokens;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kafara.voting.model.AbstractEntity;
import pl.kafara.voting.model.users.User;

import java.time.Instant;
import java.util.StringJoiner;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "purpose")
@Table(name = "safe_tokens")
public abstract class SafeToken extends AbstractEntity {

    @Column(nullable = false)
    @Setter
    private String token;

    @Column(nullable = false)
    @Setter
    private Instant expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User user;

    @Override
    public String toString() {
        return new StringJoiner(", ", SafeToken.class.getSimpleName() + "[", "]")
                .add("token='********'")
                .add("expirationDate=" + expirationDate)
                .add("user=" + user)
                .toString();
    }
}
