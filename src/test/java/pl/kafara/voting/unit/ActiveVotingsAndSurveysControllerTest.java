package pl.kafara.voting.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.kafara.voting.vote.controllers.ActiveVotingsAndSurveysController;
import pl.kafara.voting.vote.dto.ActiveVotingsAndSurveysResponse;
import pl.kafara.voting.vote.services.SurveyService;
import pl.kafara.voting.vote.services.VotingService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActiveVotingsAndSurveysControllerTest {

    @Mock
    private SurveyService surveyService;

    @Mock
    private VotingService votingService;

    @InjectMocks
    private ActiveVotingsAndSurveysController controller;

    @Test
    public void getActiveSurveys_WhenNoSurveysOrVotingsExist_ShouldReturnEmptyLists() {
        when(surveyService.getActiveSurveys()).thenReturn(Collections.emptyList());
        when(votingService.getActiveVotings()).thenReturn(Collections.emptyList());

        ResponseEntity<ActiveVotingsAndSurveysResponse> response = controller.getActiveSurveys();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().surveys().isEmpty());
        assertTrue(response.getBody().votingList().isEmpty());
    }
}