package pl.kafara.voting.model.vote.userVotes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.vote.ParliamentaryClub;
import pl.kafara.voting.model.vote.UserVoteResult;
import pl.kafara.voting.model.vote.Voting;

import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_vote_other_voting")
@DiscriminatorValue("other_voting")
public class UserVoteOther extends UserVoteVoting {

    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    private UserVoteResult voteResult;

    public UserVoteOther(Voting voting, User user, UserVoteResult voteResult, ParliamentaryClub userParliamentaryClub) {
        super(user, Period.between(user.getBirthDate().toLocalDate(), LocalDateTime.now().toLocalDate()).getYears(), user.getGender(), voting, userParliamentaryClub);
        this.voteResult = voteResult;
    }
}
