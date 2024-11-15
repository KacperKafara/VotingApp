package pl.kafara.voting.vote.controllers;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.util.filteringCriterias.VotingListFilteringCriteria;
import pl.kafara.voting.vote.dto.VotingListResponse;
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

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public VotingListResponse getVotingList(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "asc") @Pattern(regexp = "asc|desc") String sort,
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "sitting", required = false) Long sitting
    ) throws NotFoundException {
        Sort sortBy = Sort.by(Sort.Direction.fromString(sort), "date");
        Pageable pageable = PageRequest.of(page, size, sortBy);

        VotingListFilteringCriteria filteringCriteria = VotingListFilteringCriteria.builder()
                .pageable(pageable)
                .title(title)
                .sitting(sitting)
                .build();

        return votingService.getVotingListFiltered(filteringCriteria);
    }

}
