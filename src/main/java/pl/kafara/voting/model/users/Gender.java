package pl.kafara.voting.model.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kafara.voting.model.AbstractEntity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Gender extends AbstractEntity {

    @Column(nullable = false, unique = true, updatable = false)
    @Enumerated(EnumType.STRING)
    private GenderEnum name;

}
