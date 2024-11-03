package pl.kafara.voting.vote.controllers;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.model.vote.survey.Survey;
import pl.kafara.voting.util.filteringCriterias.SurveysFilteringCriteria;
import pl.kafara.voting.vote.dto.CreateSurveyRequest;
import pl.kafara.voting.vote.dto.SurveyResponse;
import pl.kafara.voting.vote.dto.SurveysResponse;
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

    @GetMapping("/latest")
    @PreAuthorize("permitAll()")
    public ResponseEntity<SurveyResponse> getLatestSurvey() throws NotFoundException {
        return ResponseEntity.ok(SurveyMapper.surveyToSurveyResponse(surveyService.getLatestSurvey()));
    }

    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') || hasRole('USER')")
    public ResponseEntity<SurveysResponse> getSurveysFiltered(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "asc") @Pattern(regexp = "asc|desc") String sort,
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "kind", defaultValue = "") @Pattern(regexp = "PARLIAMENTARY_CLUB|OTHER") String kind
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
    public ResponseEntity<SurveyResponse> createSurvey(@Validated @RequestBody CreateSurveyRequest request) {
        Survey survey = surveyService.create(SurveyMapper.createSurveyRequestToSurvey(request));
        return ResponseEntity.ok(SurveyMapper.surveyToSurveyResponse(survey));
    }
}
