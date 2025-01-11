package pl.kafara.voting.model.vote;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import pl.kafara.voting.model.AbstractEntity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Table(
        name = "sittings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"number", "term"})
        }
)
public class Sitting extends AbstractEntity {
    private Long number;
    private String title;
    @Column(name = "term", nullable = false)
    private String term;
}
