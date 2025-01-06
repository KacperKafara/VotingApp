package pl.kafara.voting.model.vote;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "last_votings_update")
public class LastVotingsUpdate {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "last_sitting_number")
    private Sitting lastSitting;
}
