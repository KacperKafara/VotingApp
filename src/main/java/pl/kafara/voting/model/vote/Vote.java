package pl.kafara.voting.model.vote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kafara.voting.model.AbstractEntity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "votes")
public class Vote extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "envoy_id")
    private Envoy envoy;

    @Enumerated(EnumType.STRING)
    private VoteResult vote;

}
