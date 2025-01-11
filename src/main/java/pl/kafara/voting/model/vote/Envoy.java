package pl.kafara.voting.model.vote;

import jakarta.persistence.*;
import lombok.*;
import pl.kafara.voting.model.AbstractEntity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "envoys")
public class Envoy extends AbstractEntity {

    private Long inTermNumber;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private ParliamentaryClub club;
    private String firstName;
    private String lastName;
    private int numberOfVotes;
    private String email;
    private String districtName;
    private boolean active;
}
