package pl.kafara.voting.model.vote.userVotes;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import pl.kafara.voting.model.vote.UserVoteResult;

@Entity
@Getter
@Table(name = "user_vote_parliamentary_club_survey")
@DiscriminatorValue("parliamentary_club")
public class UserVoteOther extends UserVoteVoting {
    private UserVoteResult voteResult;
}
