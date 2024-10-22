package pl.kafara.voting.vote.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VoteAPI {
    @JsonProperty("MP")
    private Long MP;
    private String vote;
    private String club;
    private Map<String, String> listVotes;
}
