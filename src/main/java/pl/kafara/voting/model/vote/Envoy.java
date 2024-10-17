package pl.kafara.voting.model.vote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "envoys")
public class Envoy {
    @Id
    private Long id;

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
