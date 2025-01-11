package pl.kafara.voting.model.vote;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.kafara.voting.model.AbstractEntity;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@ToString
@Table(
        name = "parliamentary_clubs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"shortName", "term"})
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
