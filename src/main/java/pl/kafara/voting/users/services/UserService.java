package pl.kafara.voting.users.services;

import dev.samstevens.totp.secret.SecretGenerator;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.ApplicationOptimisticLockException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.exceptionCodes.UserExceptionCodes;
import pl.kafara.voting.exceptions.messages.GenericMessages;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.exceptions.user.*;
import pl.kafara.voting.model.users.*;
import pl.kafara.voting.model.users.tokens.SafeToken;
import pl.kafara.voting.model.vote.ParliamentaryClub;
import pl.kafara.voting.users.dto.*;
import pl.kafara.voting.users.mapper.UserMapper;
import pl.kafara.voting.users.repositories.GenderRepository;
import pl.kafara.voting.users.repositories.RoleRepository;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.util.AESUtils;
import pl.kafara.voting.util.filteringCriterias.FilteringCriteria;
import pl.kafara.voting.util.JwsService;
import pl.kafara.voting.util.SensitiveData;
import pl.kafara.voting.util.filteringCriterias.UsersFilteringCriteria;
import pl.kafara.voting.vote.repositories.ParliamentaryClubRepository;

import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserService {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final GenderRepository genderRepository;
    private final JwsService jwsService;
    private final AESUtils aesUtils;
    private final SecretGenerator secretGenerator;
    private final ParliamentaryClubRepository parliamentaryClubRepository;

    @Value("${sejm.current-term}")
    private String currentTerm;

    public SensitiveData resetPassword(String email) throws NotFoundException, AccountNotActiveException, NoSuchAlgorithmException, CantResetPasswordException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_WITH_EMAIL_NOT_FOUND)
        );

        if (user.getOAuthId() != null)
            throw new CantResetPasswordException(UserMessages.CANT_RESET_PASSWORD, UserExceptionCodes.CANT_RESET_PASSWORD);

        if (user.isBlocked())
            throw new AccountNotActiveException(UserMessages.USER_BLOCKED, UserExceptionCodes.USER_BLOCKED);

        if (!user.isVerified())
            throw new AccountNotActiveException(UserMessages.USER_NOT_VERIFIED, UserExceptionCodes.USER_NOT_VERIFIED);

        return tokenService.generateResetPasswordToken(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {NotFoundException.class})
    public void resetPassword(String token, ResetPasswordFormRequest password) throws VerificationTokenUsedException, VerificationTokenExpiredException, NotFoundException {
        SafeToken resetPasswordToken = tokenService.validateResetPasswordToken(token);
        User user = userRepository
                .findById(resetPasswordToken.getUser().getId())
                .orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(password.password()));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {ApplicationOptimisticLockException.class})
    public void modifyUserRoles(UUID id, Set<UserRoleEnum> roles, String tagValue) throws NotFoundException, UserMustHaveAtLeastOneRoleException, ApplicationOptimisticLockException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_NOT_FOUND));

        if (jwsService.verifySignature(tagValue, user.getId(), user.getVersion())) {
            throw new ApplicationOptimisticLockException(UserMessages.OPTIMISTIC_LOCK, UserExceptionCodes.USER_OPTIMISTIC_LOCK);
        }
        Set<Role> rolesEntities = new HashSet<>();

        for (UserRoleEnum role : roles) {
            Role roleEntity = roleRepository.findByName(role).orElseThrow(() -> new NotFoundException(UserMessages.ROLE_NOT_FOUND, UserExceptionCodes.ROLE_NOT_FOUND));
            rolesEntities.add(roleEntity);
        }

        if (rolesEntities.isEmpty())
            throw new UserMustHaveAtLeastOneRoleException(UserMessages.USER_MUST_HAVE_AT_LEAST_ONE_ROLE, UserExceptionCodes.USER_MUST_HAVE_AT_LEAST_ONE_ROLE);

        Role voterRole = roleRepository.findByName(UserRoleEnum.VOTER).orElseThrow(() -> new NotFoundException(UserMessages.ROLE_NOT_FOUND, UserExceptionCodes.ROLE_NOT_FOUND));
        Role userRole = roleRepository.findByName(UserRoleEnum.USER).orElseThrow(() -> new NotFoundException(UserMessages.ROLE_NOT_FOUND, UserExceptionCodes.ROLE_NOT_FOUND));

        boolean hasVoterRole = user.getRoles().contains(voterRole);
        user.setRoles(rolesEntities);
        if (hasVoterRole && rolesEntities.contains(userRole)) {
            user.getRoles().add(voterRole);
        }
        userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {ApplicationOptimisticLockException.class})
    public User block(UUID id, String tagValue) throws NotFoundException, ApplicationOptimisticLockException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_NOT_FOUND));
        if (jwsService.verifySignature(tagValue, user.getId(), user.getVersion())) {
            throw new ApplicationOptimisticLockException(UserMessages.OPTIMISTIC_LOCK, UserExceptionCodes.USER_OPTIMISTIC_LOCK);
        }
        user.setBlocked(true);
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {ApplicationOptimisticLockException.class})
    public User unblock(UUID id, String tagValue) throws NotFoundException, ApplicationOptimisticLockException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_NOT_FOUND));
        if (jwsService.verifySignature(tagValue, user.getId(), user.getVersion())) {
            throw new ApplicationOptimisticLockException(UserMessages.OPTIMISTIC_LOCK, UserExceptionCodes.USER_OPTIMISTIC_LOCK);
        }
        user.setBlocked(false);
        return userRepository.save(user);
    }

    @PreAuthorize("isAuthenticated()")
    public User changePassword(ChangePasswordRequest password, UUID userId) throws NotFoundException, WrongPasswordException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_NOT_FOUND));
        if (!passwordEncoder.matches(password.oldPassword(), user.getPassword()))
            throw new WrongPasswordException(UserMessages.WRONG_PASSWORD, UserExceptionCodes.WRONG_PASSWORD);

        user.setPassword(passwordEncoder.encode(password.newPassword()));
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public User getUser(String username) throws NotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_NOT_FOUND));
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public User getUserById(UUID id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_NOT_FOUND));
    }

    @PreAuthorize("permitAll()")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public User getUserByOAuthId(String oAuthId) {
        return userRepository.findByOAuthId(oAuthId).orElse(null);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public UsersResponse getUsers(UsersFilteringCriteria filteringCriteria) {
        Role role = roleRepository.findByName(UserRoleEnum.fromString(filteringCriteria.getRole())).orElse(null);
        Page<User> users;
        if (role != null)
            users = userRepository.getAllByUsernameContainsAndRolesContaining(filteringCriteria.getPageable(), filteringCriteria.getUsername(), role);
        else
            users = userRepository.getAllByUsernameContains(filteringCriteria.getPageable(), filteringCriteria.getUsername());

        List<UserResponse> usersResponse = users.getContent().stream()
                .map(UserMapper::mapToUserResponse)
                .toList();

        return new UsersResponse(
                usersResponse,
                users.getTotalPages(),
                users.getNumber(),
                users.getSize()
        );
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {ApplicationOptimisticLockException.class})
    public User updateUser(UpdateUserDataRequest userData, UUID id, String tagValue) throws NotFoundException, ApplicationOptimisticLockException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_NOT_FOUND));
        Gender gender = genderRepository.findByName(GenderEnum.valueOf(userData.gender())).orElseThrow(() -> new NotFoundException(GenericMessages.NOT_FOUND, UserExceptionCodes.NOT_FOUND));

        if (jwsService.verifySignature(tagValue, user.getId(), user.getVersion())) {
            throw new ApplicationOptimisticLockException(UserMessages.OPTIMISTIC_LOCK, UserExceptionCodes.USER_OPTIMISTIC_LOCK);
        }

        user.setUsername(userData.username());
        user.setFirstName(userData.firstName());
        user.setLastName(userData.lastName());
        user.setPhoneNumber(userData.phoneNumber());
        user.setEmail(userData.email());
        user.setGender(gender);
        return userRepository.save(user);
    }

    @PreAuthorize("isAuthenticated()")
    public void change2FaState(UUID id, boolean active) throws NotFoundException, TotpAuthorisationException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_NOT_FOUND));
        if (!active) {
            user.setAuthorisationTotpSecret(null);
            userRepository.save(user);
            return;
        } else if (user.getAuthorisationTotpSecret() != null) {
            throw new TotpAuthorisationException(UserMessages.TOTP_AUTHORISATION_ALREADY_ACTIVE, UserExceptionCodes.TOTP_AUTHORISATION_ALREADY_ACTIVE);
        }

        user.setAuthorisationTotpSecret(aesUtils.encrypt(secretGenerator.generate()));
        userRepository.save(user);
    }

    @PreAuthorize("hasRole('VOTER')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {ApplicationOptimisticLockException.class})
    public User updateParliamentaryClub(UUID userId, UUID parliamentaryClubId, String tagValue) throws NotFoundException, ApplicationOptimisticLockException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_NOT_FOUND));
        if (jwsService.verifySignature(tagValue, user.getId(), user.getVersion())) {
            throw new ApplicationOptimisticLockException(UserMessages.OPTIMISTIC_LOCK, UserExceptionCodes.USER_OPTIMISTIC_LOCK);
        }

        ParliamentaryClub parliamentaryClub = parliamentaryClubRepository.findById(parliamentaryClubId).orElseThrow(() -> new NotFoundException(UserMessages.PARLIAMENTARY_CLUB_NOT_FOUND, UserExceptionCodes.PARLIAMENTARY_CLUB_NOT_FOUND));
        if (!Objects.equals(parliamentaryClub.getTerm(), currentTerm) && !parliamentaryClub.getId().equals(UUID.fromString("00000000-0000-0000-0000-000000000000")))
            throw new NotFoundException(UserMessages.PARLIAMENTARY_CLUB_NOT_FOUND, UserExceptionCodes.PARLIAMENTARY_CLUB_NOT_FOUND);

        user.setParliamentaryClub(parliamentaryClub);
        return userRepository.save(user);
    }
}
