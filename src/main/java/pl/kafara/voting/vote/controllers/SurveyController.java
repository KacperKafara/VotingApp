package pl.kafara.voting.vote.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.model.vote.survey.Survey;
import pl.kafara.voting.vote.dto.CreateSurveyRequest;
import pl.kafara.voting.vote.dto.SurveyResponse;
import pl.kafara.voting.vote.mapper.SurveyMapper;
import pl.kafara.voting.vote.services.SurveyService;

import java.util.UUID;

@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class SurveyController {
    private final SurveyService surveyService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SurveyResponse> getSurvey(@PathVariable UUID id) throws NotFoundException {
        return ResponseEntity.ok(SurveyMapper.surveyToSurveyResponse(surveyService.getSurveyById(id)));
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<SurveyResponse> getLatestSurvey() throws NotFoundException {
        return ResponseEntity.ok(SurveyMapper.surveyToSurveyResponse(surveyService.getLatestSurvey()));
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<SurveyResponse> createSurvey(@Validated @RequestBody CreateSurveyRequest request) {
        Survey survey = surveyService.create(SurveyMapper.createSurveyRequestToSurvey(request));
        return ResponseEntity.ok(SurveyMapper.surveyToSurveyResponse(survey));
    }
}
