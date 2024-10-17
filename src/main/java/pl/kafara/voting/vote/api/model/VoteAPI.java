package pl.kafara.voting.vote.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VoteAPI {
    private int mp;
    private String club;
    private String firstName;
    private String lastName;
    private String vote;
}
