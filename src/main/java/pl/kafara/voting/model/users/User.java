package pl.kafara.voting.model.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.kafara.voting.model.AbstractEntity;
import pl.kafara.voting.model.users.tokens.AccountVerificationToken;
import pl.kafara.voting.model.users.tokens.SafeToken;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@Getter
@NoArgsConstructor
@ToString
@Setter
@SecondaryTable(name = "personal_data", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
public class User extends AbstractEntity implements Serializable {

    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @ToString.Exclude
    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role> roles = new HashSet<>();

    @Column(name = "first_name", table = "personal_data", nullable = false)
    private String firstName;
    @Column(name = "last_name", table = "personal_data", nullable = false)
    private String lastName;
    @Column(name = "phone_number", table = "personal_data", nullable = false, unique = true)
    private String phoneNumber;
    @Column(name = "birth_date", table = "personal_data", nullable = false)
    private LocalDateTime birthDate;
    @Column(name = "email", table = "personal_data", nullable = false, unique = true)
    private String email;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gender_id", table = "personal_data", nullable = false)
    private Gender gender;
    @Column(name = "language", nullable = false)
    private String language;
    @Column(name = "totp_secret", unique = true)
    @ToString.Exclude
    private String totpSecret;
    @Column(name = "authorisation_totp_secret", unique = true)
    @ToString.Exclude
    private String authorisationTotpSecret;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private VoterRoleRequest voterRoleRequest;

    @Column(name = "verified", nullable = false)
    private boolean verified = false;
    @Column(name = "blocked", nullable = false)
    private boolean blocked = false;

    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts = 0;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    @Column(name = "last_failed_login")
    private LocalDateTime lastFailedLogin;

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @ToString.Exclude
    @Column(name = "oauth_id", unique = true)
    private String oAuthId;

    public User(String firstName, String lastName, String phoneNumber, LocalDateTime birthDate, String username, String email, String language) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.username = username;
        this.email = email;
        this.language = language;
    }
}
