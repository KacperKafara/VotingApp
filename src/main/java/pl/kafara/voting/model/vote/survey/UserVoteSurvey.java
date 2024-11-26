package pl.kafara.voting.model.vote.survey;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kafara.voting.model.AbstractEntity;
import pl.kafara.voting.model.users.User;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "vote_type")
@Table(
        name = "user_vote_survey",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "survey_id"})
        }
)
public abstract class UserVoteSurvey extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false, updatable = false)
    private Survey survey;
}
