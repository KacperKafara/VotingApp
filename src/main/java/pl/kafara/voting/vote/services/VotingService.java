package pl.kafara.voting.vote.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.ApplicationOptimisticLockException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.VotingException;
import pl.kafara.voting.exceptions.exceptionCodes.VotingExceptionCodes;
import pl.kafara.voting.exceptions.messages.VotingMessages;
import pl.kafara.voting.model.vote.Sitting;
import pl.kafara.voting.model.vote.Voting;
import pl.kafara.voting.model.vote.VotingOption;
import pl.kafara.voting.util.JwsService;
import pl.kafara.voting.util.filteringCriterias.VotingListFilteringCriteria;
import pl.kafara.voting.vote.api.repositories.SittingRepository;
import pl.kafara.voting.vote.dto.SittingResponse;
import pl.kafara.voting.vote.dto.VotingListResponse;
import pl.kafara.voting.vote.dto.VotingWithoutVotesResponse;
import pl.kafara.voting.vote.mapper.SittingMapper;
import pl.kafara.voting.vote.mapper.VotingMapper;
import pl.kafara.voting.vote.repositories.VotingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class VotingService {
    private final VotingRepository votingRepository;
    private final SittingRepository sittingRepository;
    private final JwsService jwsService;

    @PreAuthorize("hasRole('USER')")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Voting getVotingById(UUID id) throws NotFoundException {
        return votingRepository.findById(id).orElseThrow(() -> new NotFoundException(VotingMessages.VOTING_NOT_FOUND, VotingExceptionCodes.VOTING_NOT_FOUND));
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public VotingListResponse getVotingListFiltered(VotingListFilteringCriteria filteringCriteria) throws NotFoundException {
        Page<Voting> votingPage;

        if(filteringCriteria.getSitting() == null || filteringCriteria.getSitting() == UUID.fromString("00000000-0000-0000-0000-000000000000")) {
            votingPage = votingRepository.getVotingByTitleContains(
                    filteringCriteria.getPageable(),
                    filteringCriteria.getTitle(),
                    filteringCriteria.isWasActive()
            );
        } else {
            Sitting sitting = sittingRepository.findById(filteringCriteria.getSitting()).orElseThrow(() -> new NotFoundException(VotingMessages.SITTING_NOT_FOUND, VotingExceptionCodes.SITTING_NOT_FOUND));
            votingPage = votingRepository.getVotingByTitleContainsAndSitting(
                    filteringCriteria.getPageable(),
                    filteringCriteria.getTitle(),
                    sitting,
                    filteringCriteria.isWasActive()
            );
        }

        List<VotingWithoutVotesResponse> votingList = votingPage.getContent().stream()
                .map(VotingMapper::votingToVotingWithoutVotesResponse)
                .toList();

        List<SittingResponse> sittingIds = sittingRepository.findAll().stream()
                .map(SittingMapper::sittingToSittingResponse)
                .toList();

        return new VotingListResponse(
                votingList,
                sittingIds,
                votingPage.getTotalPages(),
                votingPage.getNumber(),
                votingPage.getSize()
        );
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<Voting> getActiveVotings() {
        return votingRepository.getAllByEndDateAfterNow();
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @Transactional(propagation = Propagation.REQUIRED)
    public void startVoting(UUID votingId, LocalDateTime endDate, String tagValue) throws NotFoundException, ApplicationOptimisticLockException, VotingException {
        Voting voting = getVotingById(votingId);
        if(jwsService.verifySignature(tagValue, voting.getId(), voting.getVersion())){
            throw new ApplicationOptimisticLockException(VotingMessages.OPTIMISTIC_LOCK_EXCEPTION, VotingExceptionCodes.OPTIMISTIC_LOCK_EXCEPTION);
        }

        if (voting.getEndDate() != null) {
            throw new VotingException(VotingMessages.VOTING_ALREADY_ACTIVE, VotingExceptionCodes.VOTING_ALREADY_ACTIVE);
        }
        voting.setEndDate(endDate);
        votingRepository.save(voting);
    }

    @PreAuthorize("hasRole('VOTER')")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<VotingOption> getVotingOptionsForVoting(UUID votingId) throws NotFoundException {
        Voting voting = getVotingById(votingId);
        return voting.getVotingOptions();
    }
}
