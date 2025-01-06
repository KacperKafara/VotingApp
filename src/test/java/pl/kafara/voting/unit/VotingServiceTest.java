package pl.kafara.voting.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kafara.voting.exceptions.ApplicationOptimisticLockException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.VotingException;
import pl.kafara.voting.model.vote.Voting;
import pl.kafara.voting.util.JwsService;
import pl.kafara.voting.vote.api.repositories.SittingRepository;
import pl.kafara.voting.vote.repositories.VotingRepository;
import pl.kafara.voting.vote.services.VotingService;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VotingServiceTest {
    @Mock
    VotingRepository votingRepository;
    @Mock
    SittingRepository sittingRepository;
    @Mock
    JwsService jwsService;

    @InjectMocks
    VotingService votingService;

    @Test
    public void startVoting_WhenVotingNotFound_ShouldThrowNotFoundException() {
        UUID votingId = UUID.randomUUID();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        String tagValue = "tagValue";

        when(votingRepository.findById(votingId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> votingService.startVoting(votingId, endDate, tagValue));
    }

    @Test
    public void startVoting_WhenTagValueIsInvalid_ShouldThrowApplicationOptimisticLockException() throws NoSuchFieldException, IllegalAccessException {
        UUID votingId = UUID.randomUUID();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        String tagValue = "tagValue";
        Voting voting = new Voting();
        Field idField = voting.getClass().getSuperclass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(voting, votingId);

        Field versionField = voting.getClass().getSuperclass().getDeclaredField("version");
        versionField.setAccessible(true);
        versionField.set(voting, 1L);

        when(votingRepository.findById(votingId)).thenReturn(Optional.of(voting));
        when(jwsService.verifySignature(tagValue, voting.getId(), voting.getVersion())).thenReturn(true);

        assertThrows(ApplicationOptimisticLockException.class, () -> votingService.startVoting(votingId, endDate, tagValue));
    }

    @Test
    public void startVoting_WhenVotingAlreadyActive_ShouldThrowVotingException() throws IllegalAccessException, NoSuchFieldException {
        UUID votingId = UUID.randomUUID();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        String tagValue = "tagValue";
        Voting voting = new Voting();
        Field idField = voting.getClass().getSuperclass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(voting, votingId);
        voting.setEndDate(LocalDateTime.now().plusDays(1));

        when(votingRepository.findById(votingId)).thenReturn(Optional.of(voting));
        when(jwsService.verifySignature(tagValue, voting.getId(), voting.getVersion())).thenReturn(false);

        assertThrows(VotingException.class, () -> votingService.startVoting(votingId, endDate, tagValue));
    }

    @Test
    public void startVoting_ValidData_ShouldStartVoting() throws NotFoundException, ApplicationOptimisticLockException, VotingException, IllegalAccessException, NoSuchFieldException {
        UUID votingId = UUID.randomUUID();
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        String tagValue = "tagValue";
        Voting voting = new Voting();
        Field idField = voting.getClass().getSuperclass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(voting, votingId);

        when(votingRepository.findById(votingId)).thenReturn(Optional.of(voting));
        when(jwsService.verifySignature(tagValue, voting.getId(), voting.getVersion())).thenReturn(false);

        votingService.startVoting(votingId, endDate, tagValue);

        assertEquals(endDate, voting.getEndDate());
        verify(votingRepository, times(1)).save(voting);
    }
}
