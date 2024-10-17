package pl.kafara.voting.vote.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EnvoyAPI {
    private Long id;
    private String club;
    private String firstName;
    private String lastName;
    private int numberOfVotes;
    private String email;
    private String districtName;
    private boolean active;
}