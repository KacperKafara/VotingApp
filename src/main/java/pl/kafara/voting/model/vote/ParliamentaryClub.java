package pl.kafara.voting.model.vote;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import pl.kafara.voting.model.AbstractEntity;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(
        name = "parliamentary_clubs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"short_name", "term"})
        }
)
public class ParliamentaryClub extends AbstractEntity implements Serializable {

    private String shortName;
    @Column(name = "term", nullable = false)
    private String term;

    private String email;
    private String fax;
    private Integer membersCount;
    private String name;
    private String phone;
}
