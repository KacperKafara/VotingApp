package pl.kafara.voting.vote.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParliamentaryClubAPI {
    private String id;
    private String email;
    private String fax;
    private Integer membersCount;
    private String name;
    private String phone;
}
