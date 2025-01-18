package pl.kafara.voting.model.vote.survey;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.vote.ParliamentaryClub;

import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_vote_parliamentary_club_survey")
@DiscriminatorValue("parliamentary_club")
public class UserVoteParliamentaryClub extends UserVoteSurvey {
    @ManyToOne
    @JoinColumn(name = "parliamentary_club_id", updatable = false)
    private ParliamentaryClub parliamentaryClub;

    public UserVoteParliamentaryClub(Survey survey, User user, ParliamentaryClub parliamentaryClub, ParliamentaryClub userParliamentaryClub) {
        super(user, Period.between(user.getBirthDate().toLocalDate(), LocalDateTime.now().toLocalDate()).getYears(), user.getGender(), survey, userParliamentaryClub);
        this.parliamentaryClub = parliamentaryClub;
    }
}
