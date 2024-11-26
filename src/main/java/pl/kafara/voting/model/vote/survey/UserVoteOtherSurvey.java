package pl.kafara.voting.model.vote.survey;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kafara.voting.model.vote.UserVoteResult;
import pl.kafara.voting.model.users.User;

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
        super(user, survey);
        this.result = result;
    }
}
