package pl.kafara.voting.users.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.samstevens.totp.secret.SecretGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.exceptionCodes.UserExceptionCodes;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.exceptions.user.ResolveOwnRequestException;
import pl.kafara.voting.exceptions.user.RoleRequestException;
import pl.kafara.voting.exceptions.user.YouAreVoterException;
import pl.kafara.voting.model.users.*;
import pl.kafara.voting.users.dto.VoterRoleRequestListResponse;
import pl.kafara.voting.users.dto.VoterRoleRequestResponse;
import pl.kafara.voting.users.mapper.RoleRequestMapper;
import pl.kafara.voting.users.repositories.RoleRepository;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.users.repositories.VoterRoleRequestRepository;
import pl.kafara.voting.util.AESUtils;
import pl.kafara.voting.util.filteringCriterias.RoleRequestFilteringCriteria;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class VoterRoleRequestService {
    private final VoterRoleRequestRepository roleRequestRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AESUtils aesUtils;
    private final SecretGenerator secretGenerator;

    @PreAuthorize("hasRole('USER')")
    public void createRequest(UUID id) throws NotFoundException, YouAreVoterException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_NOT_FOUND));
        Role voter = roleRepository.findByName(UserRoleEnum.VOTER).orElseThrow(() ->
                new NotFoundException(UserMessages.ROLE_NOT_FOUND, UserExceptionCodes.ROLE_NOT_FOUND));

        if (user.getRoles().contains(voter))
            throw new YouAreVoterException(UserMessages.USER_ALREADY_HAS_VOTER_ROLE, UserExceptionCodes.USER_ALREADY_HAS_VOTER_ROLE);

        Optional<VoterRoleRequest> roleRequestOptional = roleRequestRepository.getVoterRoleRequestByUser_Id(id);

        if(roleRequestOptional.isPresent()){
            VoterRoleRequest voterRoleRequest = roleRequestOptional.get();
            if(voterRoleRequest.getResolution().equals(RoleRequestResolution.REJECTED)) {
                voterRoleRequest.setResolution(RoleRequestResolution.PENDING);
                voterRoleRequest.setRequestDate(LocalDateTime.now());
                roleRequestRepository.save(voterRoleRequest);
                return;
            }
        }

        VoterRoleRequest roleRequest = new VoterRoleRequest(user);
        roleRequestRepository.save(roleRequest);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {NotFoundException.class, ResolveOwnRequestException.class})
    public User acceptRequest(UUID id) throws NotFoundException, ResolveOwnRequestException, RoleRequestException {
        DecodedJWT jwt =  JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UUID currentId = UUID.fromString(jwt.getSubject());
        VoterRoleRequest roleRequest = roleRequestRepository.findById(id).orElseThrow(() ->
                new NotFoundException(UserMessages.ROLE_REQUEST_NOT_FOUND, UserExceptionCodes.ROLE_REQUEST_NOT_FOUND));
        if (roleRequest.getResolution() != RoleRequestResolution.PENDING)
            throw new RoleRequestException(UserMessages.ROLE_REQUEST_ALREADY_RESOLVED, UserExceptionCodes.ROLE_REQUEST_ALREADY_RESOLVED);

        User user = roleRequest.getUser();
        Role role = roleRepository.findByName(UserRoleEnum.VOTER).orElseThrow(() ->
                new NotFoundException(UserMessages.ROLE_NOT_FOUND, UserExceptionCodes.ROLE_NOT_FOUND));

        if (currentId.equals(user.getId()))
            throw new ResolveOwnRequestException(UserMessages.ACCEPT_OWN_REQUEST_ROLE, UserExceptionCodes.ACCEPT_OWN_REQUEST_ROLE);

        roleRequest.setResolution(RoleRequestResolution.ACCEPTED);
        user.getRoles().add(role);
        user.setTotpSecret(aesUtils.encrypt(secretGenerator.generate()));
        roleRequestRepository.save(roleRequest);
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public User rejectRequest(UUID id) throws NotFoundException, ResolveOwnRequestException, RoleRequestException {
        DecodedJWT jwt =  JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UUID currentId = UUID.fromString(jwt.getSubject());
        VoterRoleRequest roleRequest = roleRequestRepository.findById(id).orElseThrow(() ->
                new NotFoundException(UserMessages.ROLE_REQUEST_NOT_FOUND, UserExceptionCodes.ROLE_REQUEST_NOT_FOUND));

        if(roleRequest.getResolution() != RoleRequestResolution.PENDING)
            throw new RoleRequestException(UserMessages.ROLE_REQUEST_ALREADY_RESOLVED, UserExceptionCodes.ROLE_REQUEST_ALREADY_RESOLVED);

        if (currentId.equals(roleRequest.getUser().getId()))
            throw new ResolveOwnRequestException(UserMessages.REJECT_OWN_REQUEST_ROLE, UserExceptionCodes.REJECT_OWN_REQUEST_ROLE);

        roleRequest.setResolution(RoleRequestResolution.REJECTED);
        roleRequestRepository.save(roleRequest);
        return roleRequest.getUser();
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public VoterRoleRequestListResponse getRoleRequests(RoleRequestFilteringCriteria filteringCriteria) {
        Page<VoterRoleRequest> roleRequests = roleRequestRepository.getAllByUser_UsernameContainsAndResolutionEquals(filteringCriteria.getPageable(), filteringCriteria.getUsername(), RoleRequestResolution.PENDING);

        List<VoterRoleRequestResponse> responses = roleRequests.getContent().stream()
                .map(RoleRequestMapper::toVoterRoleRequestResponse)
                .toList();

        return new VoterRoleRequestListResponse(
                responses,
                roleRequests.getTotalPages(),
                roleRequests.getNumber(),
                roleRequests.getSize()
        );
    }
}
