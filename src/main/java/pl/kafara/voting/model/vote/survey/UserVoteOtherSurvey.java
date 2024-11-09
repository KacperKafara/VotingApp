package pl.kafara.voting.model.vote.survey;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "user_vote_other_survey")
@DiscriminatorValue("other_survey")
public class UserVoteOtherSurvey extends UserVote {
    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    private UserVoteResult result;
}
