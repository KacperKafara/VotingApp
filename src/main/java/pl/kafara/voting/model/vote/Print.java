package pl.kafara.voting.model.vote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prints")
public class Print {
    @Id
    @Column(name = "number", updatable = false)
    private String number;
    @Column(length = 1000)
    private String title;
    private String url;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Voting> votings;

    public Print(String number, String title, String url) {
        this.number = number;
        this.title = title;
        this.url = url;
    }
}
