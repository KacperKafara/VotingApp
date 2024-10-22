package pl.kafara.voting.model.vote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class VotingOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String option;
    private int optionIndex;
    private int votes;

    @ManyToOne
    @JoinColumn(name = "voting_id")
    @Setter
    private Voting voting;

}
