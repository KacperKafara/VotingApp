package pl.kafara.voting.model.vote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Vote {

    private int mp;
    private String club;
    private String firstName;
    private String lastName;
    private String vote;

}
