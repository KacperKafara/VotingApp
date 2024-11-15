package pl.kafara.voting.vote.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.exceptionCodes.VotingExceptionCodes;
import pl.kafara.voting.exceptions.messages.VotingMessages;
import pl.kafara.voting.model.vote.Sitting;
import pl.kafara.voting.model.vote.Voting;
import pl.kafara.voting.util.filteringCriterias.VotingListFilteringCriteria;
import pl.kafara.voting.vote.api.repositories.SittingRepository;
import pl.kafara.voting.vote.dto.VotingListResponse;
import pl.kafara.voting.vote.dto.VotingWithoutVotesResponse;
import pl.kafara.voting.vote.mapper.VotingMapper;
import pl.kafara.voting.vote.repositories.VotingRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class VotingService {
    private final VotingRepository votingRepository;
    private final SittingRepository sittingRepository;

    @PreAuthorize("hasRole('USER')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Voting getVotingById(UUID id) throws NotFoundException {
        return votingRepository.findById(id).orElseThrow(() -> new NotFoundException(VotingMessages.VOTING_NOT_FOUND, VotingExceptionCodes.VOTING_NOT_FOUND));
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public VotingListResponse getVotingListFiltered(VotingListFilteringCriteria filteringCriteria) throws NotFoundException {
        Page<Voting> votingPage;

        if(filteringCriteria.getSitting() == null || filteringCriteria.getSitting() == 0) {
            votingPage = votingRepository.getVotingByTitleContains(
                    filteringCriteria.getPageable(),
                    filteringCriteria.getTitle()
            );
        } else {
            Sitting sitting = sittingRepository.findById(Math.toIntExact(filteringCriteria.getSitting())).orElseThrow(() -> new NotFoundException(VotingMessages.SITTING_NOT_FOUND, VotingExceptionCodes.SITTING_NOT_FOUND));
            votingPage = votingRepository.getVotingByTitleContainsAndSitting(
                    filteringCriteria.getPageable(),
                    filteringCriteria.getTitle(),
                    sitting
            );
        }

        List<VotingWithoutVotesResponse> votingList = votingPage.getContent().stream()
                .map(VotingMapper::votingToVotingWithoutVotesResponse)
                .toList();

        List<Long> sittingIds = sittingRepository.findAll().stream()
                .map(Sitting::getNumber)
                .toList();

        return new VotingListResponse(
                votingList,
                sittingIds,
                votingPage.getTotalPages(),
                votingPage.getNumber(),
                votingPage.getSize()
        );
    }

}
