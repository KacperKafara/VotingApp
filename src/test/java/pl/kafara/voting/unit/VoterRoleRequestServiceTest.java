package pl.kafara.voting.unit;

import dev.samstevens.totp.secret.SecretGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.user.ResolveOwnRequestException;
import pl.kafara.voting.exceptions.user.RoleRequestException;
import pl.kafara.voting.exceptions.user.YouAreVoterException;
import pl.kafara.voting.model.users.*;
import pl.kafara.voting.users.dto.VoterRoleRequestListResponse;
import pl.kafara.voting.users.repositories.RoleRepository;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.users.repositories.VoterRoleRequestRepository;
import pl.kafara.voting.users.services.VoterRoleRequestService;
import pl.kafara.voting.util.AESUtils;
import pl.kafara.voting.util.filteringCriterias.RoleRequestFilteringCriteria;

import java.lang.reflect.Field;
import java.util.List;
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

    @Test
    public void createRequest_WhenRequestAlreadyExistsAndIsRejected_ShouldUpdateRequest() throws NotFoundException, YouAreVoterException {
        UUID userId = UUID.randomUUID();
        User user = new User();
        Role voterRole = new Role(UserRoleEnum.VOTER);
        VoterRoleRequest existingRequest = new VoterRoleRequest(user);
        existingRequest.setResolution(RoleRequestResolution.REJECTED);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(UserRoleEnum.VOTER)).thenReturn(Optional.of(voterRole));
        when(roleRequestRepository.getVoterRoleRequestByUser_Id(userId)).thenReturn(Optional.of(existingRequest));

        voterRoleRequestService.createRequest(userId);

        assertEquals(RoleRequestResolution.PENDING, existingRequest.getResolution());
        verify(roleRequestRepository, times(1)).save(existingRequest);
    }

    @Test
    public void acceptRequest_WhenRequestAlreadyResolved_ShouldThrowRoleRequestException() {
        UUID requestId = UUID.randomUUID();
        VoterRoleRequest request = new VoterRoleRequest(new User());
        request.setResolution(RoleRequestResolution.ACCEPTED);

        when(roleRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        assertThrows(RoleRequestException.class, () -> voterRoleRequestService.acceptRequest(requestId));
    }

    @Test
    public void acceptRequest_WhenUserIsSameAsCurrentUser_ShouldThrowResolveOwnRequestException() throws NoSuchFieldException, IllegalAccessException {
        UUID requestId = UUID.randomUUID();
        UUID userId = UUID.fromString("5b8e3d65-8043-47c3-a038-83d419454be7");
        User user = new User();
        Field idField = user.getClass().getSuperclass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, userId);
        VoterRoleRequest request = new VoterRoleRequest(user);
        request.setResolution(RoleRequestResolution.PENDING);

        when(roleRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(roleRepository.findByName(UserRoleEnum.VOTER)).thenReturn(Optional.of(new Role(UserRoleEnum.VOTER)));

        assertThrows(ResolveOwnRequestException.class, () -> voterRoleRequestService.acceptRequest(requestId));
    }

    @Test
    public void acceptRequest_ValidRequest_ShouldUpdateUserAndRequest() throws Exception {
        UUID requestId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        User user = new User();
        Field idField = user.getClass().getSuperclass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, userId);
        VoterRoleRequest request = new VoterRoleRequest(user);
        request.setResolution(RoleRequestResolution.PENDING);
        Role voterRole = new Role(UserRoleEnum.VOTER);

        when(roleRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(roleRepository.findByName(UserRoleEnum.VOTER)).thenReturn(Optional.of(voterRole));
        when(secretGenerator.generate()).thenReturn("secret");
        when(aesUtils.encrypt("secret")).thenReturn("encryptedSecret");

        voterRoleRequestService.acceptRequest(requestId);

        assertEquals(RoleRequestResolution.ACCEPTED, request.getResolution());
        assertTrue(user.getRoles().contains(voterRole));
        verify(roleRequestRepository, times(1)).save(request);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void rejectRequest_WhenRequestAlreadyResolved_ShouldThrowRoleRequestException() {
        UUID requestId = UUID.randomUUID();
        VoterRoleRequest request = new VoterRoleRequest(new User());
        request.setResolution(RoleRequestResolution.REJECTED);

        when(roleRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        assertThrows(RoleRequestException.class, () -> voterRoleRequestService.rejectRequest(requestId));
    }
}
