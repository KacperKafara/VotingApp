package pl.kafara.voting.vote.dto;

import pl.kafara.voting.model.vote.Print;
import pl.kafara.voting.model.vote.VotingKind;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record VotingResponse(
        UUID id,
        String title,
        String description,
        String topic,
        LocalDateTime date,
        VotingKind kind,
        List<VoteResponse> votes,
        List<Print> prints
) {
}
