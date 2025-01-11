package pl.kafara.voting.model.vote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.kafara.voting.model.AbstractEntity;

import java.util.List;

@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prints")
public class Print extends AbstractEntity {
    @Column(name = "number", updatable = false)
    private String number;
    @Column(length = 1000)
    private String title;
    private String url;
    @Column(name = "term", nullable = false)
    private String term;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Voting> votings;

    public Print(String number, String title, String url, String term) {
        this.number = number;
        this.title = title;
        this.url = url;
        this.term = term;
    }
}
