package pl.kafara.voting.vote.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kafara.voting.vote.dto.ActiveVotingsAndSurveysResponse;
import pl.kafara.voting.vote.dto.SurveyWithoutVotesResponse;
import pl.kafara.voting.vote.dto.VotingWithoutVotesResponse;
import pl.kafara.voting.vote.mapper.SurveyMapper;
import pl.kafara.voting.vote.mapper.VotingMapper;
import pl.kafara.voting.vote.services.SurveyService;
import pl.kafara.voting.vote.services.VotingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activeSurveys")
@Transactional(propagation = Propagation.NEVER)
public class ActiveVotingsAndSurveysController {

    private final SurveyService surveyService;
    private final VotingService votingService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ActiveVotingsAndSurveysResponse> getActiveSurveys() {
        List<SurveyWithoutVotesResponse> surveys = surveyService.getActiveSurveys().stream().map(
                SurveyMapper::surveyToSurveyWithoutVotesResponse
        ).toList();

        List<VotingWithoutVotesResponse> votings = votingService.getActiveVotings().stream().map(
                VotingMapper::votingToVotingWithoutVotesResponse
        ).toList();

        ActiveVotingsAndSurveysResponse response = new ActiveVotingsAndSurveysResponse(
                votings,
                surveys
        );


        return ResponseEntity.ok(response);
    }
}
