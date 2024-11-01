package pl.kafara.voting.model.vote.survey;

import jakarta.persistence.*;
import pl.kafara.voting.model.vote.ParliamentaryClub;

@Entity
@Table(name = "user_vote_parliamentary_club")
@DiscriminatorValue("parliamentary_club")
public class UserVoteParliamentaryClub extends UserVote {
    @ManyToOne
    @JoinColumn(name = "parliamentary_club_id")
    private ParliamentaryClub parliamentaryClub;
}
