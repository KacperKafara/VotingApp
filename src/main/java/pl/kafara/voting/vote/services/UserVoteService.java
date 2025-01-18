package pl.kafara.voting.vote.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.SurveyException;
import pl.kafara.voting.exceptions.VotingOrSurveyNotActiveException;
import pl.kafara.voting.exceptions.exceptionCodes.SurveyExceptionCodes;
import pl.kafara.voting.exceptions.exceptionCodes.VotingExceptionCodes;
import pl.kafara.voting.exceptions.messages.SurveyMessages;
import pl.kafara.voting.exceptions.messages.VotingMessages;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.vote.*;
import pl.kafara.voting.model.vote.survey.Survey;
import pl.kafara.voting.model.vote.survey.SurveyKind;
import pl.kafara.voting.model.vote.survey.UserVoteOtherSurvey;
import pl.kafara.voting.model.vote.survey.UserVoteParliamentaryClub;
import pl.kafara.voting.model.vote.userVotes.UserVoteOnList;
import pl.kafara.voting.model.vote.userVotes.UserVoteOther;
import pl.kafara.voting.vote.repositories.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public class UserVoteService {
    private final SurveyService surveyService;
    private final ParliamentaryClubRepository parliamentaryClubRepository;
    private final VotingService votingService;
    private final VotingOptionRepository votingOptionRepository;
    private final UserVoteOnListRepository userVoteOnListRepository;
    private final ParliamentaryClubVoteRepository parliamentaryClubVoteRepository;
    private final OtherSurveyVoteRepository otherSurveyVoteRepository;
    private final UserVoteOtherRepository votingOtherRepository;

    @Value("${sejm.current-term}")
    private String currentTerm;

    @PreAuthorize("hasRole('VOTER')")
    @CacheEvict(value = "latestSurvey", key="'latest'", condition = "#surveyId != null")
    public void voteParliamentaryClub(UUID surveyId, String parliamentaryClubId, User user) throws NotFoundException, VotingOrSurveyNotActiveException, SurveyException {
        Survey survey = surveyService.getSurveyById(surveyId);

        if (survey.getSurveyKind() != SurveyKind.PARLIAMENTARY_CLUB) {
            throw new SurveyException(SurveyMessages.SURVEY_KIND_NOT_PARLIAMENTARY_CLUB, SurveyExceptionCodes.SURVEY_KIND_NOT_PARLIAMENTARY_CLUB);
        }

        if (survey.getEndDate().isBefore(LocalDateTime.now())) {
            throw new VotingOrSurveyNotActiveException(SurveyMessages.SURVEY_NOT_ACTIVE, SurveyExceptionCodes.SURVEY_NOT_ACTIVE);
        }

        UUID parliamentaryClubIdUUID;
        try {
            parliamentaryClubIdUUID = UUID.fromString(parliamentaryClubId);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException(SurveyMessages.PARLIAMENTARY_CLUB_NOT_FOUND, SurveyExceptionCodes.PARLIAMENTARY_CLUB_NOT_FOUND);
        }

        ParliamentaryClub parliamentaryClub = parliamentaryClubRepository.findById(parliamentaryClubIdUUID).orElseThrow(
                () -> new NotFoundException(SurveyMessages.PARLIAMENTARY_CLUB_NOT_FOUND, SurveyExceptionCodes.PARLIAMENTARY_CLUB_NOT_FOUND)
        );

        if (!Objects.equals(parliamentaryClub.getTerm(), currentTerm))
            throw new NotFoundException(SurveyMessages.PARLIAMENTARY_CLUB_NOT_FOUND, SurveyExceptionCodes.PARLIAMENTARY_CLUB_NOT_FOUND);

        parliamentaryClubVoteRepository.save(new UserVoteParliamentaryClub(survey, user, parliamentaryClub, user.getParliamentaryClub()));
    }

    @PreAuthorize("hasRole('VOTER')")
    @CacheEvict(value = "latestSurvey", key="'latest'", condition = "#surveyId != null")
    public void voteOtherSurvey(UUID surveyId, UserVoteResult userVoteResult, User user) throws NotFoundException, VotingOrSurveyNotActiveException, SurveyException {
        Survey survey = surveyService.getSurveyById(surveyId);

        if (survey.getSurveyKind() != SurveyKind.OTHER) {
            throw new SurveyException(SurveyMessages.SURVEY_KIND_NOT_OTHER, SurveyExceptionCodes.SURVEY_KIND_NOT_OTHER);
        }

        if (survey.getEndDate().isBefore(LocalDateTime.now())) {
            throw new VotingOrSurveyNotActiveException(SurveyMessages.SURVEY_NOT_ACTIVE, SurveyExceptionCodes.SURVEY_NOT_ACTIVE);
        }

        otherSurveyVoteRepository.save(new UserVoteOtherSurvey(survey, user, userVoteResult, user.getParliamentaryClub()));
    }

    @PreAuthorize("hasRole('VOTER')")
    public void voteVoting(UUID votingId, UserVoteResult userVoteResult, User user) throws NotFoundException, VotingOrSurveyNotActiveException, SurveyException {
        Voting voting = votingService.getVotingById(votingId);

        if (!voting.getVotingOptions().isEmpty() || voting.getKind() == VotingKind.ON_LIST) {
            throw new SurveyException(VotingMessages.VOTING_ON_LIST, VotingExceptionCodes.VOTING_ON_LIST);
        }

        if (voting.getEndDate().isBefore(LocalDateTime.now())) {
            throw new VotingOrSurveyNotActiveException(VotingMessages.VOTING_NOT_ACTIVE, VotingExceptionCodes.VOTING_NOT_ACTIVE);
        }

        votingOtherRepository.save(new UserVoteOther(voting, user, userVoteResult, user.getParliamentaryClub()));
    }

    @PreAuthorize("hasRole('VOTER')")
    public void voteOnList(UUID votingId, UUID votingOptionId, User user) throws NotFoundException, VotingOrSurveyNotActiveException, SurveyException {
        Voting voting = votingService.getVotingById(votingId);

        if (voting.getVotingOptions().isEmpty() || voting.getKind() != VotingKind.ON_LIST) {
            throw new SurveyException(VotingMessages.VOTING_NOT_ON_LIST, VotingExceptionCodes.VOTING_NOT_ON_LIST);
        }

        if (voting.getEndDate().isBefore(LocalDateTime.now())) {
            throw new VotingOrSurveyNotActiveException(VotingMessages.VOTING_NOT_ACTIVE, VotingExceptionCodes.VOTING_NOT_ACTIVE);
        }

        VotingOption votingOption = votingOptionRepository.findByIdAndVoting(votingOptionId, voting).orElseThrow(
                () -> new NotFoundException(SurveyMessages.VOTING_OPTION_NOT_FOUND, SurveyExceptionCodes.VOTING_OPTION_NOT_FOUND)
        );

        userVoteOnListRepository.save(new UserVoteOnList(voting, user, votingOption, user.getParliamentaryClub()));
    }
}
