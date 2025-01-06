package pl.kafara.voting.model.vote;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "voting_options")
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
