package pl.kafara.voting.vote.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.samstevens.totp.code.CodeVerifier;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.SurveyException;
import pl.kafara.voting.exceptions.TotpException;
import pl.kafara.voting.exceptions.VotingOrSurveyNotActiveException;
import pl.kafara.voting.exceptions.exceptionCodes.SurveyExceptionCodes;
import pl.kafara.voting.exceptions.messages.GenericMessages;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.vote.UserVoteResult;
import pl.kafara.voting.model.vote.VotingOption;
import pl.kafara.voting.users.services.UserService;
import pl.kafara.voting.util.AESUtils;
import pl.kafara.voting.util.filteringCriterias.VotingListFilteringCriteria;
import pl.kafara.voting.vote.dto.*;
import pl.kafara.voting.vote.mapper.VotingMapper;
import pl.kafara.voting.vote.mapper.VotingOptionMapper;
import pl.kafara.voting.vote.services.UserVoteService;
import pl.kafara.voting.vote.services.VotingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/votings")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class VotingController {
    private final VotingService votingService;
    private final UserService userService;
    private final CodeVerifier codeVerifier;
    private final AESUtils aesUtils;
    private final UserVoteService userVoteService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VotingResponse> getVoting(@PathVariable UUID id) throws NotFoundException {
        DecodedJWT jwt = JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(VotingMapper.votingToVotingResponse(votingService.getVotingById(id), userId));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<VotingListResponse> getVotingList(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "asc") @Pattern(regexp = "asc|desc") String sort,
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "sitting", required = false) Long sitting,
            @RequestParam(name = "wasActive", defaultValue = "false") boolean wasActive
    ) throws NotFoundException {
        Sort sortBy = Sort.by(Sort.Direction.fromString(sort), "date");
        Pageable pageable = PageRequest.of(page, size, sortBy);

        VotingListFilteringCriteria filteringCriteria = VotingListFilteringCriteria.builder()
                .pageable(pageable)
                .title(title)
                .sitting(sitting)
                .wasActive(wasActive)
                .build();

        return ResponseEntity.ok(votingService.getVotingListFiltered(filteringCriteria));
    }

    @PostMapping("/{id}/vote")
    @PreAuthorize("hasRole('VOTER')")
    public ResponseEntity<Void> vote(@PathVariable UUID id, @Validated @RequestBody CreateUserVoteRequest request) throws NotFoundException {
        DecodedJWT jwt = JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UUID userId = UUID.fromString(jwt.getSubject());
        User user = userService.getUserById(userId);
        if (!codeVerifier.isValidCode(aesUtils.decrypt(user.getTotpSecret()), request.totp())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GenericMessages.INVALID_TOTP, new TotpException(GenericMessages.INVALID_TOTP, SurveyExceptionCodes.INVALID_TOTP));
        }
        try {
            UUID voteResult = UUID.fromString(request.voteResult());
            userVoteService.voteOnList(id, voteResult, user);
        } catch (IllegalArgumentException ignored) {
            try {
                UserVoteResult voteResult = UserVoteResult.fromString(request.voteResult());
                userVoteService.voteVoting(id, voteResult, user);
            } catch (VotingOrSurveyNotActiveException | SurveyException | IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            }
        } catch (VotingOrSurveyNotActiveException | SurveyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Void> startVoting(@PathVariable UUID id, @Validated @RequestBody ActivateVotingRequest request) throws NotFoundException {
        votingService.startVoting(id, request.endDate());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/votingOptions")
    @PreAuthorize("hasRole('VOTER')")
    public ResponseEntity<List<VotingOptionResponse>> getVotingOptions(@PathVariable UUID id) throws NotFoundException {
        List<VotingOption> votingOptions = votingService.getVotingOptionsForVoting(id);
        List<VotingOptionResponse> votingOptionResponses = votingOptions.stream()
                .map(VotingOptionMapper::votingOptionToVotingOptionResponse)
                .toList();

        return ResponseEntity.ok(votingOptionResponses);
    }

    @GetMapping("/{id}/details")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<VotingDetailsResponse> getVotingDetails(@PathVariable UUID id) throws NotFoundException {
        return ResponseEntity.ok(VotingMapper.votingToVotingDetailsResponse(votingService.getVotingById(id)));
    }

}
