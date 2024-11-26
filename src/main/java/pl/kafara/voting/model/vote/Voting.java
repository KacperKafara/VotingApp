package pl.kafara.voting.model.vote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kafara.voting.model.AbstractEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "votings")
public class Voting extends AbstractEntity {
    private int votingNumber;
    @ManyToOne
    @JoinColumn(name = "sitting_number")
    private Sitting sitting;
    private int sittingDay;
    private LocalDateTime date;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "voting")
    private List<VotingOption> votingOptions = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "voting")
    private List<Vote> votes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private VotingKind kind;

    private LocalDateTime endDate;

    @Column(length = 1000)
    private String title;
    @Column(length = 1000)
    private String description;
    @Column(length = 1000)
    private String topic;

    @ManyToMany
    private List<Print> prints;
}
