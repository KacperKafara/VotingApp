package pl.kafara.voting.model.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.kafara.voting.model.AbstractEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@Getter
@NoArgsConstructor
@ToString
@Setter
@SecondaryTable(name = "personal_data", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
public class User extends AbstractEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Role> roles;

    @Column(name = "first_name", table = "personal_data", nullable = false)
    private String firstName;
    @Column(name = "last_name", table = "personal_data", nullable = false)
    private String lastName;
    @Column(name = "phone_number", table = "personal_data", nullable = false)
    private String phoneNumber;
    @Column(name = "birth_date", table = "personal_data", nullable = false)
    private LocalDateTime birthDate;
}
