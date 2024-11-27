package pl.kafara.voting.model.vote.userVotes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.vote.Voting;
import pl.kafara.voting.model.vote.VotingOption;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_vote_on_list")
@DiscriminatorValue("on_list")
public class UserVoteOnList extends UserVoteVoting {

    @ManyToOne
    @JoinColumn(name = "voting_option_id")
    private VotingOption votingOption;

    public UserVoteOnList(Voting voting, User user, VotingOption votingOption) {
        super(user, voting);
        this.votingOption = votingOption;
    }
}
