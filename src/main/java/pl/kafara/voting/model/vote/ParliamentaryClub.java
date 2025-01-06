package pl.kafara.voting.model.vote;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@ToString
@Table(name = "parliamentary_clubs")
public class ParliamentaryClub implements Serializable {
    @Id
    private String id;
    private String email;
    private String fax;
    private Integer membersCount;
    private String name;
    private String phone;
}
