package pl.kafara.voting.model.vote;

import jakarta.persistence.*;
import lombok.*;
import pl.kafara.voting.model.AbstractEntity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "votes")
@ToString
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
