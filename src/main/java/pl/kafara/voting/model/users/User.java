package pl.kafara.voting.model.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.kafara.voting.model.AbstractEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @ToString.Exclude
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Role> roles = new ArrayList<>();

    @Column(name = "first_name", table = "personal_data", nullable = false)
    private String firstName;
    @Column(name = "last_name", table = "personal_data", nullable = false)
    private String lastName;
    @Column(name = "phone_number", table = "personal_data", nullable = false)
    private String phoneNumber;
    @Column(name = "birth_date", table = "personal_data", nullable = false)
    private LocalDateTime birthDate;

    @Column(name = "verified", nullable = false)
    private boolean verified = false;
    @Column(name = "blocked", nullable = false)
    private boolean blocked = false;

    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts = 0;

    public User(String firstName, String lastName, String phoneNumber, LocalDateTime birthDate, String username, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.username = username;
        this.email = email;
    }
}
