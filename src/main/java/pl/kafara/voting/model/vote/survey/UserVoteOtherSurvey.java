package pl.kafara.voting.model.vote.survey;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kafara.voting.model.vote.UserVoteResult;
import pl.kafara.voting.model.users.User;

import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_vote_other_survey")
@DiscriminatorValue("other_survey")
public class UserVoteOtherSurvey extends UserVoteSurvey {
    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    private UserVoteResult result;

    public UserVoteOtherSurvey(Survey survey, User user, UserVoteResult result) {
        super(user, Period.between(user.getBirthDate().toLocalDate(), LocalDateTime.now().toLocalDate()).getYears(), user.getGender(), survey);
        this.result = result;
    }
}
