package pl.kafara.voting.model.vote.survey;

import jakarta.persistence.*;
import lombok.Getter;
import pl.kafara.voting.model.vote.UserVoteResult;

@Entity
@Getter
@Table(name = "user_vote_other_survey")
@DiscriminatorValue("other_survey")
public class UserVoteOtherSurvey extends UserVoteSurvey {
    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    private UserVoteResult result;
}
