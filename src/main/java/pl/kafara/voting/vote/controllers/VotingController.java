package pl.kafara.voting.vote.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.vote.dto.VotingResponse;
import pl.kafara.voting.vote.mapper.VotingMapper;
import pl.kafara.voting.vote.services.VotingService;

import java.util.UUID;

@RestController
@RequestMapping("/votings")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class VotingController {
    private final VotingService votingService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public VotingResponse getVoting(@PathVariable UUID id) throws NotFoundException {
        return VotingMapper.votingToVotingResponse(votingService.getVotingById(id));
    }

}
