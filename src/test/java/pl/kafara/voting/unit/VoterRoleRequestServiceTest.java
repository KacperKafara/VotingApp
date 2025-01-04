package pl.kafara.voting.unit;

import dev.samstevens.totp.secret.SecretGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.user.YouAreVoterException;
import pl.kafara.voting.model.users.*;
import pl.kafara.voting.users.repositories.RoleRepository;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.users.repositories.VoterRoleRequestRepository;
import pl.kafara.voting.users.services.VoterRoleRequestService;
import pl.kafara.voting.util.AESUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class VoterRoleRequestServiceTest {
    @Mock
    VoterRoleRequestRepository roleRequestRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    AESUtils aesUtils;
    @Mock
    SecretGenerator secretGenerator;
    @InjectMocks
    VoterRoleRequestService voterRoleRequestService;
    @Mock
    SecurityContext securityContext;
    @Mock
    Authentication authentication;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.setContext(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getPrincipal()).thenReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI1YjhlM2Q2NS04MDQzLTQ3YzMtYTAzOC04M2Q0MTk0NTRiZTciLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.oiQA77vO7w42OXxWCI-IuiN29kKsLgFNMsFDWxQuIgs");
    }

    @Test
    public void createRequest_UserNotFound_ShouldThrowNotFoundException() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> voterRoleRequestService.createRequest(userId));
    }

    @Test
    public void createRequest_UserAlreadyVoter_ShouldThrowYouAreVoterException() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        Role voterRole = new Role(UserRoleEnum.VOTER);
        user.setRoles(Set.of(voterRole));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(UserRoleEnum.VOTER)).thenReturn(Optional.of(voterRole));

        assertThrows(YouAreVoterException.class, () -> voterRoleRequestService.createRequest(userId));
    }

    @Test
    public void createRequest_ValidRequest_ShouldSaveRequest() throws NotFoundException, YouAreVoterException {
        UUID userId = UUID.randomUUID();
        User user = new User();
        Role voterRole = new Role(UserRoleEnum.VOTER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(UserRoleEnum.VOTER)).thenReturn(Optional.of(voterRole));
        when(roleRequestRepository.getVoterRoleRequestByUser_Id(userId)).thenReturn(Optional.empty());

        voterRoleRequestService.createRequest(userId);

        verify(roleRequestRepository, times(1)).save(any(VoterRoleRequest.class));
    }

    @Test
    public void acceptRequest_RequestNotFound_ShouldThrowNotFoundException() {
        UUID requestId = UUID.randomUUID();
        when(roleRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> voterRoleRequestService.acceptRequest(requestId));
    }

    @Test
    public void rejectRequest_RequestNotFound_ShouldThrowNotFoundException() {
        UUID requestId = UUID.randomUUID();
        when(roleRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> voterRoleRequestService.rejectRequest(requestId));
    }

    @Test
    public void rejectRequest_ValidRequest_ShouldUpdateRequest() throws Exception {
        UUID requestId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        User user = new User();
        Field ifField = user.getClass().getSuperclass().getDeclaredField("id");
        ifField.setAccessible(true);
        ifField.set(user, userId);
        VoterRoleRequest request = new VoterRoleRequest(user);
        request.setResolution(RoleRequestResolution.PENDING);

        when(roleRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        voterRoleRequestService.rejectRequest(requestId);

        assertEquals(RoleRequestResolution.REJECTED, request.getResolution());
        verify(roleRequestRepository, times(1)).save(request);
    }
}
