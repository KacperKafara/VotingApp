package pl.kafara.voting.vote.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.exceptionCodes.VotingExceptionCodes;
import pl.kafara.voting.exceptions.messages.VotingMessages;
import pl.kafara.voting.model.vote.Voting;
import pl.kafara.voting.vote.repositories.VotingRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class VotingService {
    private final VotingRepository votingRepository;

    @PreAuthorize("hasRole('USER')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Voting getVotingById(UUID id) throws NotFoundException {
        return votingRepository.findById(id).orElseThrow(() -> new NotFoundException(VotingMessages.VOTING_NOT_FOUND, VotingExceptionCodes.VOTING_NOT_FOUND));
    }

}
