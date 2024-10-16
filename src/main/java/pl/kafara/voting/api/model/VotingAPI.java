package pl.kafara.voting.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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
}
