package pl.kafara.voting.model.vote;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "parliamentary_clubs")
public class ParliamentaryClub {
    @Id
    private String id;
    private String email;
    private String fax;
    private Integer membersCount;
    private String name;
    private String phone;
}
