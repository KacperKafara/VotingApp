package pl.kafara.voting.vote.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.kafara.voting.model.vote.VotingOption;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VotingAPI {
    private int votingNumber;
    private int sitting;
    private int term;
    private int sittingDay;
    private int yes;
    private int no;
    private int abstain;
    private int notParticipating;
    private int totalVoted;
    private LocalDateTime date;
    private String title;
    private String description;
    private String topic;
    private String kind;
    private List<VotingOption> votingOptions;
    private List<VoteAPI> votes;
}
