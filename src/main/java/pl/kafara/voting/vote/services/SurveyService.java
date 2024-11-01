package pl.kafara.voting.vote.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.exceptionCodes.SurveyExceptionCodes;
import pl.kafara.voting.exceptions.messages.SurveyMessages;
import pl.kafara.voting.model.vote.survey.Survey;
import pl.kafara.voting.vote.repositories.SurveyRepository;

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
        return surveyRepository.findLatest().orElseThrow(
                () -> new NotFoundException(SurveyMessages.SURVEY_NOT_FOUND, SurveyExceptionCodes.SURVEY_NOT_FOUND)
        );
    }
}
