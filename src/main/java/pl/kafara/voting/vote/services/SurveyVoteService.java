package pl.kafara.voting.vote.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.exceptionCodes.SurveyExceptionCodes;
import pl.kafara.voting.exceptions.messages.SurveyMessages;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.vote.ParliamentaryClub;
import pl.kafara.voting.model.vote.UserVoteResult;
import pl.kafara.voting.model.vote.survey.Survey;
import pl.kafara.voting.model.vote.survey.UserVoteOtherSurvey;
import pl.kafara.voting.model.vote.survey.UserVoteParliamentaryClub;
import pl.kafara.voting.vote.repositories.ParliamentaryClubRepository;
import pl.kafara.voting.vote.repositories.OtherSurveyVoteRepository;
import pl.kafara.voting.vote.repositories.ParliamentaryClubVoteRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SurveyVoteService {
    private final ParliamentaryClubVoteRepository parliamentaryClubVoteRepository;
    private final OtherSurveyVoteRepository otherSurveyVoteRepository;
    private final SurveyService surveyService;
    private final ParliamentaryClubRepository parliamentaryClubRepository;

    @PreAuthorize("hasRole('VOTER')")
    public void voteParliamentaryClub(UUID surveyId, String parliamentaryClubId, User user) throws NotFoundException {
        Survey survey = surveyService.getSurveyById(surveyId);
        ParliamentaryClub parliamentaryClub = parliamentaryClubRepository.findById(parliamentaryClubId).orElseThrow(
                () -> new NotFoundException(SurveyMessages.PARLIAMENTARY_CLUB_NOT_FOUND, SurveyExceptionCodes.PARLIAMENTARY_CLUB_NOT_FOUND)
        );

        parliamentaryClubVoteRepository.save(new UserVoteParliamentaryClub(survey, user, parliamentaryClub));
    }

    @PreAuthorize("hasRole('VOTER')")
    public void voteOtherSurvey(UUID surveyId, UserVoteResult userVoteResult, User user) throws NotFoundException {
        Survey survey = surveyService.getSurveyById(surveyId);

        otherSurveyVoteRepository.save(new UserVoteOtherSurvey(survey, user, userVoteResult));
    }
}
