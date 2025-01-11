package pl.kafara.voting.model.vote;

import jakarta.persistence.*;
import lombok.*;
import pl.kafara.voting.model.AbstractEntity;

@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "voting_options")
public class VotingOption extends AbstractEntity {

    private String option;
    private int optionIndex;
    private int votes;

    @ManyToOne
    @JoinColumn(name = "voting_id")
    @Setter
    private Voting voting;

}
