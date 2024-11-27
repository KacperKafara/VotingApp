package pl.kafara.voting.model.vote.userVotes;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.vote.UserVoteResult;
import pl.kafara.voting.model.vote.Voting;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_vote_other_voting")
@DiscriminatorValue("other_voting")
public class UserVoteOther extends UserVoteVoting {
    private UserVoteResult voteResult;

    public UserVoteOther(Voting voting, User user, UserVoteResult voteResult) {
        super(user, voting);
        this.voteResult = voteResult;
    }
}
