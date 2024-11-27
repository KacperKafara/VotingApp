package pl.kafara.voting.vote.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.CodeGenerationException;
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
import pl.kafara.voting.exceptions.TotpException;
import pl.kafara.voting.exceptions.exceptionCodes.SurveyExceptionCodes;
import pl.kafara.voting.exceptions.messages.GenericMessages;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.vote.UserVoteResult;
import pl.kafara.voting.model.vote.survey.Survey;
import pl.kafara.voting.users.services.UserService;
import pl.kafara.voting.util.AESUtils;
import pl.kafara.voting.util.filteringCriterias.SurveysFilteringCriteria;
import pl.kafara.voting.vote.dto.*;
import pl.kafara.voting.vote.mapper.SurveyMapper;
import pl.kafara.voting.vote.services.SurveyService;
import pl.kafara.voting.vote.services.UserVoteService;

import java.util.UUID;

@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class SurveyController {
    private final SurveyService surveyService;
    private final CodeVerifier codeVerifier;
    private final UserService userService;
    private final UserVoteService userVoteService;
    private final AESUtils aesUtils;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SurveyResponse> getSurvey(@PathVariable UUID id) throws NotFoundException {
        DecodedJWT jwt =  JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UUID userId = UUID.fromString(jwt.getSubject());
        Survey survey = surveyService.getSurveyById(id);
        return ResponseEntity.ok(SurveyMapper.surveyToSurveyResponse(survey, userId));
    }

    @GetMapping("/latest")
    @PreAuthorize("permitAll()")
    public ResponseEntity<SurveyResponse> getLatestSurvey() throws NotFoundException {
        return ResponseEntity.ok(SurveyMapper.surveyToSurveyResponse(surveyService.getLatestSurvey(), null));
    }

    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') || hasRole('USER')")
    public ResponseEntity<SurveysResponse> getSurveysFiltered(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "asc") @Pattern(regexp = "asc|desc") String sort,
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "kind", defaultValue = "") String kind
    ) {
        Sort sortBy = Sort.by(Sort.Direction.fromString(sort), "createdAt");
        Pageable pageable = PageRequest.of(page, size, sortBy);

        SurveysFilteringCriteria filteringCriteria = SurveysFilteringCriteria.builder()
                .pageable(pageable)
                .title(title)
                .kind(kind)
                .build();

        return ResponseEntity.ok(surveyService.getSurveysFiltered(filteringCriteria));
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<SurveyWithoutVotesResponse> createSurvey(@Validated @RequestBody CreateSurveyRequest request) {
        Survey survey = surveyService.create(SurveyMapper.createSurveyRequestToSurvey(request));
        return ResponseEntity.ok(SurveyMapper.surveyToSurveyWithoutVotesResponse(survey));
    }

    @PostMapping("/{id}/vote")
    @PreAuthorize("hasRole('VOTER')")
    public ResponseEntity<Void> vote(@PathVariable UUID id, @Validated @RequestBody CreateUserVoteRequest request) throws NotFoundException, CodeGenerationException {
        DecodedJWT jwt =  JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UUID userId = UUID.fromString(jwt.getSubject());
        User user = userService.getUserById(userId);
        if(!codeVerifier.isValidCode(aesUtils.decrypt(user.getTotpSecret()), request.totp())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GenericMessages.INVALID_TOTP, new TotpException(GenericMessages.INVALID_TOTP, SurveyExceptionCodes.INVALID_TOTP));
        }
        try {
            UserVoteResult voteResult = UserVoteResult.fromString(request.voteResult());
            userVoteService.voteOtherSurvey(id, voteResult, user);
        } catch (IllegalArgumentException e) {
            userVoteService.voteParliamentaryClub(id, request.voteResult(), user);
        }
        return ResponseEntity.ok().build();
    }
}
