package pl.kafara.voting.model.vote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kafara.voting.model.AbstractEntity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "votes")
@Setter
public class Vote extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "envoy_id")
    private Envoy envoy;

    @Enumerated(EnumType.STRING)
    private VoteResult vote;

    private String club;

    @ManyToOne
    @JoinColumn(name = "voting_option_id")
    private VotingOption votingOption;

    @ManyToOne
    @JoinColumn(name = "voting_id")
    private Voting voting;
}
