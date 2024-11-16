package pl.kafara.voting.model.vote.userVotes;

import jakarta.persistence.*;
import lombok.Getter;
import pl.kafara.voting.model.vote.VotingOption;

@Entity
@Getter
@Table(name = "user_vote_on_list")
@DiscriminatorValue("parliamentary_club")
public class UserVoteOnList extends UserVoteVoting {

    @ManyToOne
    @JoinColumn(name = "voting_option_id")
    private VotingOption votingOption;
}
