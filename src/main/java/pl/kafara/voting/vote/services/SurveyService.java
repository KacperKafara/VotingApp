package pl.kafara.voting.vote.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.exceptionCodes.SurveyExceptionCodes;
import pl.kafara.voting.exceptions.messages.SurveyMessages;
import pl.kafara.voting.model.vote.survey.Survey;
import pl.kafara.voting.model.vote.survey.SurveyKind;
import pl.kafara.voting.util.filteringCriterias.SurveysFilteringCriteria;
import pl.kafara.voting.vote.dto.SurveyWithoutVotesResponse;
import pl.kafara.voting.vote.dto.SurveysResponse;
import pl.kafara.voting.vote.mapper.SurveyMapper;
import pl.kafara.voting.vote.repositories.SurveyRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SurveyService {
    private final SurveyRepository surveyRepository;

    @PreAuthorize("hasRole('MODERATOR')")
    public Survey create(Survey survey) {
        return surveyRepository.save(survey);
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Survey getSurveyById(UUID id) throws NotFoundException {
        return surveyRepository.findById(id).orElseThrow(
                () -> new NotFoundException(SurveyMessages.SURVEY_NOT_FOUND, SurveyExceptionCodes.SURVEY_NOT_FOUND)
        );
    }

    @PreAuthorize("permitAll()")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Survey getLatestSurvey() throws NotFoundException {
        return surveyRepository.findFirstByOrderByCreatedAtDesc().orElseThrow(
                () -> new NotFoundException(SurveyMessages.SURVEY_NOT_FOUND, SurveyExceptionCodes.SURVEY_NOT_FOUND)
        );
    }

    @PreAuthorize("hasRole('MODERATOR') || hasRole('USER')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public SurveysResponse getSurveysFiltered(SurveysFilteringCriteria filteringCriteria) {
        Page<Survey> surveysPage;
        if(filteringCriteria.getKind() == null || filteringCriteria.getKind().isEmpty())
            surveysPage = surveyRepository.findAllByTitleContains(
                    filteringCriteria.getPageable(),
                    filteringCriteria.getTitle()
            );
        else
            surveysPage = surveyRepository.findAllByTitleContainsAndSurveyKind(
                    filteringCriteria.getPageable(),
                    filteringCriteria.getTitle(),
                    SurveyKind.fromString(filteringCriteria.getKind())
            );

        List<SurveyWithoutVotesResponse> surveyResponses = surveysPage.getContent().stream()
                .map(SurveyMapper::surveyToSurveyWithoutVotesResponse)
                .toList();

        return new SurveysResponse(
                surveyResponses,
                surveysPage.getTotalPages(),
                surveysPage.getNumber(),
                surveysPage.getSize()
        );
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<Survey> getActiveSurveys() {
        return surveyRepository.findAllByEndDateBeforeNow();
    }
}
