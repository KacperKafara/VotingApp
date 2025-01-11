package pl.kafara.voting.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.SurveyException;
import pl.kafara.voting.exceptions.VotingOrSurveyNotActiveException;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.vote.*;
import pl.kafara.voting.model.vote.survey.Survey;
import pl.kafara.voting.model.vote.survey.SurveyKind;
import pl.kafara.voting.model.vote.survey.UserVoteOtherSurvey;
import pl.kafara.voting.model.vote.survey.UserVoteParliamentaryClub;
import pl.kafara.voting.model.vote.userVotes.UserVoteOther;
import pl.kafara.voting.vote.repositories.*;
import pl.kafara.voting.vote.services.SurveyService;
import pl.kafara.voting.vote.services.UserVoteService;
import pl.kafara.voting.vote.services.VotingService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserVoteServiceTest {

    @Mock
    private SurveyService surveyService;
    @Mock
    private ParliamentaryClubRepository parliamentaryClubRepository;
    @Mock
    private VotingService votingService;
    @Mock
    private VotingOptionRepository votingOptionRepository;
    @Mock
    private UserVoteOnListRepository userVoteOnListRepository;
    @Mock
    private ParliamentaryClubVoteRepository parliamentaryClubVoteRepository;
    @Mock
    private OtherSurveyVoteRepository otherSurveyVoteRepository;
    @Mock
    private UserVoteOtherRepository votingOtherRepository;

    @InjectMocks
    private UserVoteService userVoteService;

    @Test
    public void voteParliamentaryClub_WhenSurveyNotFound_ShouldThrowNotFoundException() throws NotFoundException {
        UUID surveyId = UUID.randomUUID();
        String parliamentaryClubId = "clubId";
        User user = new User();

        when(surveyService.getSurveyById(surveyId)).thenThrow(new NotFoundException("Survey not found"));

        assertThrows(NotFoundException.class, () -> {
            userVoteService.voteParliamentaryClub(surveyId, parliamentaryClubId, user);
        });
    }

    @Test
    public void voteParliamentaryClub_WhenSurveyNotActive_ShouldThrowVotingOrSurveyNotActiveException() throws NotFoundException {
        UUID surveyId = UUID.randomUUID();
        String parliamentaryClubId = "clubId";
        User user = new User();
        Survey survey = new Survey();
        survey.setEndDate(LocalDateTime.now().minusDays(1));
        survey.setSurveyKind(SurveyKind.PARLIAMENTARY_CLUB);

        when(surveyService.getSurveyById(surveyId)).thenReturn(survey);

        assertThrows(VotingOrSurveyNotActiveException.class, () -> {
            userVoteService.voteParliamentaryClub(surveyId, parliamentaryClubId, user);
        });
    }

    @Test
    public void voteParliamentaryClub_WhenSurveyKindNotParliamentaryClub_ShouldThrowSurveyException() throws NotFoundException {
        UUID surveyId = UUID.randomUUID();
        String parliamentaryClubId = "clubId";
        User user = new User();
        Survey survey = new Survey();
        survey.setSurveyKind(SurveyKind.OTHER);

        when(surveyService.getSurveyById(surveyId)).thenReturn(survey);

        assertThrows(SurveyException.class, () -> {
            userVoteService.voteParliamentaryClub(surveyId, parliamentaryClubId, user);
        });
    }

//    @Test
//    public void voteParliamentaryClub_WhenValid_ShouldSaveVote() throws NotFoundException, VotingOrSurveyNotActiveException, SurveyException {
//        UUID surveyId = UUID.randomUUID();
//        UUID parliamentaryClubId = UUID.randomUUID();
//        User user = new User();
//        user.setBirthDate(LocalDateTime.now().minusYears(18));
//        Survey survey = new Survey();
//        survey.setSurveyKind(SurveyKind.PARLIAMENTARY_CLUB);
//        survey.setEndDate(LocalDateTime.now().plusDays(1));
//        ParliamentaryClub parliamentaryClub = new ParliamentaryClub();
//
//        when(surveyService.getSurveyById(surveyId)).thenReturn(survey);
//        when(parliamentaryClubRepository.findById(parliamentaryClubId)).thenReturn(Optional.of(parliamentaryClub));
//
//        userVoteService.voteParliamentaryClub(surveyId, parliamentaryClubId, user);
//
//        verify(parliamentaryClubVoteRepository).save(any(UserVoteParliamentaryClub.class));
//    }

    @Test
    public void voteOtherSurvey_WhenSurveyNotFound_ShouldThrowNotFoundException() throws NotFoundException {
        UUID surveyId = UUID.randomUUID();
        UserVoteResult userVoteResult = UserVoteResult.DEFINITELY_YES;
        User user = new User();

        when(surveyService.getSurveyById(surveyId)).thenThrow(new NotFoundException("Survey not found"));

        assertThrows(NotFoundException.class, () -> {
            userVoteService.voteOtherSurvey(surveyId, userVoteResult, user);
        });
    }

    @Test
    public void voteOtherSurvey_WhenSurveyNotActive_ShouldThrowVotingOrSurveyNotActiveException() throws NotFoundException {
        UUID surveyId = UUID.randomUUID();
        UserVoteResult userVoteResult = UserVoteResult.DEFINITELY_YES;
        User user = new User();
        Survey survey = new Survey();
        survey.setSurveyKind(SurveyKind.OTHER);
        survey.setEndDate(LocalDateTime.now().minusDays(1));

        when(surveyService.getSurveyById(surveyId)).thenReturn(survey);

        assertThrows(VotingOrSurveyNotActiveException.class, () -> {
            userVoteService.voteOtherSurvey(surveyId, userVoteResult, user);
        });
    }

    @Test
    public void voteOtherSurvey_WhenSurveyKindNotOther_ShouldThrowSurveyException() throws NotFoundException {
        UUID surveyId = UUID.randomUUID();
        UserVoteResult userVoteResult = UserVoteResult.DEFINITELY_YES;
        User user = new User();
        Survey survey = new Survey();
        survey.setSurveyKind(SurveyKind.PARLIAMENTARY_CLUB);

        when(surveyService.getSurveyById(surveyId)).thenReturn(survey);

        assertThrows(SurveyException.class, () -> {
            userVoteService.voteOtherSurvey(surveyId, userVoteResult, user);
        });
    }

    @Test
    public void voteOtherSurvey_WhenValid_ShouldSaveVote() throws NotFoundException, VotingOrSurveyNotActiveException, SurveyException {
        UUID surveyId = UUID.randomUUID();
        UserVoteResult userVoteResult = UserVoteResult.DEFINITELY_YES;
        User user = new User();
        user.setBirthDate(LocalDateTime.now().minusYears(18));
        Survey survey = new Survey();
        survey.setSurveyKind(SurveyKind.OTHER);
        survey.setEndDate(LocalDateTime.now().plusDays(1));

        when(surveyService.getSurveyById(surveyId)).thenReturn(survey);

        userVoteService.voteOtherSurvey(surveyId, userVoteResult, user);

        verify(otherSurveyVoteRepository).save(any(UserVoteOtherSurvey.class));
    }

    @Test
    public void voteVoting_WhenVotingNotFound_ShouldThrowNotFoundException() throws NotFoundException {
        UUID votingId = UUID.randomUUID();
        UserVoteResult userVoteResult = UserVoteResult.DEFINITELY_YES;
        User user = new User();

        when(votingService.getVotingById(votingId)).thenThrow(new NotFoundException("Voting not found"));

        assertThrows(NotFoundException.class, () -> {
            userVoteService.voteVoting(votingId, userVoteResult, user);
        });
    }

    @Test
    public void voteVoting_WhenVotingNotActive_ShouldThrowVotingOrSurveyNotActiveException() throws NotFoundException {
        UUID votingId = UUID.randomUUID();
        UserVoteResult userVoteResult = UserVoteResult.DEFINITELY_YES;
        User user = new User();
        Voting voting = new Voting();
        voting.setEndDate(LocalDateTime.now().minusDays(1));

        when(votingService.getVotingById(votingId)).thenReturn(voting);

        assertThrows(VotingOrSurveyNotActiveException.class, () -> {
            userVoteService.voteVoting(votingId, userVoteResult, user);
        });
    }

    @Test
    public void voteVoting_WhenVotingKindOnList_ShouldThrowSurveyException() throws NotFoundException {
        UUID votingId = UUID.randomUUID();
        UserVoteResult userVoteResult = UserVoteResult.DEFINITELY_YES;
        User user = new User();
        Voting voting = new Voting();
        user.setBirthDate(LocalDateTime.now().minusYears(18));
        voting.setKind(VotingKind.ON_LIST);
        voting.setEndDate(LocalDateTime.now().plusDays(1));

        when(votingService.getVotingById(votingId)).thenReturn(voting);

        assertThrows(SurveyException.class, () -> {
            userVoteService.voteVoting(votingId, userVoteResult, user);
        });
    }

    @Test
    public void voteVoting_WhenValid_ShouldSaveVote() throws NotFoundException, VotingOrSurveyNotActiveException, SurveyException {
        UUID votingId = UUID.randomUUID();
        UserVoteResult userVoteResult = UserVoteResult.DEFINITELY_YES;
        User user = new User();
        user.setBirthDate(LocalDateTime.now().minusYears(18));
        Voting voting = new Voting();
        voting.setKind(VotingKind.ELECTRONIC);
        voting.setEndDate(LocalDateTime.now().plusDays(1));

        when(votingService.getVotingById(votingId)).thenReturn(voting);

        userVoteService.voteVoting(votingId, userVoteResult, user);

        verify(votingOtherRepository).save(any(UserVoteOther.class));
    }

    @Test
    public void voteOnList_WhenVotingNotFound_ShouldThrowNotFoundException() throws NotFoundException {
        UUID votingId = UUID.randomUUID();
        UUID votingOptionId = UUID.randomUUID();
        User user = new User();

        when(votingService.getVotingById(votingId)).thenThrow(new NotFoundException("Voting not found"));

        assertThrows(NotFoundException.class, () -> {
            userVoteService.voteOnList(votingId, votingOptionId, user);
        });
    }

    @Test
    public void voteOnList_WhenVotingNotActive_ShouldThrowVotingOrSurveyNotActiveException() throws NotFoundException {
        UUID votingId = UUID.randomUUID();
        UUID votingOptionId = UUID.randomUUID();
        VotingOption votingOption = new VotingOption();
        User user = new User();
        Voting voting = new Voting();
        voting.setKind(VotingKind.ON_LIST);
        voting.getVotingOptions().add(votingOption);
        voting.setEndDate(LocalDateTime.now().minusDays(1));

        when(votingService.getVotingById(votingId)).thenReturn(voting);

        assertThrows(VotingOrSurveyNotActiveException.class, () -> {
            userVoteService.voteOnList(votingId, votingOptionId, user);
        });
    }

    @Test
    public void voteOnList_WhenVotingKindNotOnList_ShouldThrowSurveyException() throws NotFoundException {
        UUID votingId = UUID.randomUUID();
        UUID votingOptionId = UUID.randomUUID();
        User user = new User();
        Voting voting = new Voting();
        voting.setKind(VotingKind.TRADITIONAL);

        when(votingService.getVotingById(votingId)).thenReturn(voting);

        assertThrows(SurveyException.class, () -> {
            userVoteService.voteOnList(votingId, votingOptionId, user);
        });
    }

}
