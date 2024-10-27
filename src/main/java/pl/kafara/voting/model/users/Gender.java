package pl.kafara.voting.model.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.kafara.voting.model.AbstractEntity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@ToString
@Table(name = "genders")
public class Gender extends AbstractEntity {

    @Column(nullable = false, unique = true, updatable = false)
    @Enumerated(EnumType.STRING)
    private GenderEnum name;

}
