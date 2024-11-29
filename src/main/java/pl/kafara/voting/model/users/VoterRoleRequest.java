package pl.kafara.voting.model.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kafara.voting.model.AbstractEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class VoterRoleRequest extends AbstractEntity implements Serializable {

    @Column(name = "request_date", nullable = false)
    @Setter
    LocalDateTime requestDate = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Setter
    @Column(name = "state", nullable = false)
    private RoleRequestResolution resolution = RoleRequestResolution.PENDING;

    public VoterRoleRequest(User user) {
        this.user = user;
    }
}
