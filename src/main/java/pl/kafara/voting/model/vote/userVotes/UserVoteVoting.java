package pl.kafara.voting.model.vote.userVotes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kafara.voting.model.AbstractEntity;
import pl.kafara.voting.model.users.Gender;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.vote.Voting;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "vote_type")
@Table(
        name = "user_vote_voting",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "voting_id"})
        }
)
public abstract class UserVoteVoting extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private int age;
    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = false, updatable = false)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "voting_id", nullable = false)
    private Voting voting;
}
