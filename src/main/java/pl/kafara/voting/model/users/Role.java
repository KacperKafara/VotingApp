package pl.kafara.voting.model.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.kafara.voting.model.AbstractEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@ToString
public class Role extends AbstractEntity {

    @Column(nullable = false, unique = true, updatable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum name;

}
