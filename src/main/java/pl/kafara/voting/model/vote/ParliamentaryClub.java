package pl.kafara.voting.model.vote;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class ParliamentaryClub {
    @Id
    private String id;
    private String email;
    private String fax;
    private Integer membersCount;
    private String name;
    private String phone;
}
