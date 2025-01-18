package pl.kafara.voting.model.vote.survey;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kafara.voting.model.AbstractEntity;
import pl.kafara.voting.model.users.Gender;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.vote.ParliamentaryClub;

import java.io.Serializable;

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
public abstract class UserVoteSurvey extends AbstractEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private int age;
    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = false, updatable = false)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false, updatable = false)
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "parliamentary_club_id", nullable = false, updatable = false)
    private ParliamentaryClub parliamentaryClub;
}
