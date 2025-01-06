package pl.kafara.voting.model.vote;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Table(name = "sittings")
public class Sitting {

    @Id
    private Long number;
    private String title;
}
