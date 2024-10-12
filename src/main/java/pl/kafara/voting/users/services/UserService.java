package pl.kafara.voting.users.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.handlers.ExceptionCodes;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.exceptions.user.*;
import pl.kafara.voting.model.users.Role;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.users.UserRoleEnum;
import pl.kafara.voting.model.users.tokens.SafeToken;
import pl.kafara.voting.users.dto.ChangePasswordRequest;
import pl.kafara.voting.users.dto.ResetPasswordFormRequest;
import pl.kafara.voting.users.dto.UserResponse;
import pl.kafara.voting.users.dto.UsersResponse;
import pl.kafara.voting.users.mapper.UserMapper;
import pl.kafara.voting.users.repositories.RoleRepository;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.util.FilteringCriteria;
import pl.kafara.voting.util.SensitiveData;

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

    public SensitiveData resetPassword(String email) throws NotFoundException, AccountNotActiveException, NoSuchAlgorithmException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(UserMessages.USER_NOT_FOUND, ExceptionCodes.USER_WITH_EMAIL_NOT_FOUND)
        );

        if(user.isBlocked())
            throw new AccountNotActiveException(UserMessages.USER_BLOCKED, ExceptionCodes.USER_BLOCKED);

        if(!user.isVerified())
            throw new AccountNotActiveException(UserMessages.USER_NOT_VERIFIED, ExceptionCodes.USER_NOT_VERIFIED);

        return tokenService.generateResetPasswordToken(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {NotFoundException.class})
    public void resetPassword(String token, ResetPasswordFormRequest password) throws VerificationTokenUsedException, VerificationTokenExpiredException, NotFoundException {
        SafeToken resetPasswordToken = tokenService.validateResetPasswordToken(token);
        User user = userRepository
                .findById(resetPasswordToken.getUser().getId())
                .orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, ExceptionCodes.USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(password.password()));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void modifyUserRoles(UUID id, Set<UserRoleEnum> roles) throws NotFoundException, UserMustHaveAtLeastOneRoleException {

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, ExceptionCodes.USER_NOT_FOUND));
        Set<Role> rolesEntities = new HashSet<>();

        for (UserRoleEnum role : roles) {
            Role roleEntity = roleRepository.findByName(role).orElseThrow(() -> new NotFoundException(UserMessages.ROLE_NOT_FOUND, ExceptionCodes.ROLE_NOT_FOUND));
            rolesEntities.add(roleEntity);
        }

        if(rolesEntities.isEmpty())
            throw new UserMustHaveAtLeastOneRoleException(UserMessages.USER_MUST_HAVE_AT_LEAST_ONE_ROLE, ExceptionCodes.USER_MUST_HAVE_AT_LEAST_ONE_ROLE);

        Role voterRole = roleRepository.findByName(UserRoleEnum.VOTER).orElseThrow(() -> new NotFoundException(UserMessages.ROLE_NOT_FOUND, ExceptionCodes.ROLE_NOT_FOUND));
        Role userRole = roleRepository.findByName(UserRoleEnum.USER).orElseThrow(() -> new NotFoundException(UserMessages.ROLE_NOT_FOUND, ExceptionCodes.ROLE_NOT_FOUND));

        boolean hasVoterRole = user.getRoles().contains(voterRole);
        user.setRoles(rolesEntities);
        if(hasVoterRole && rolesEntities.contains(userRole)) {
            user.getRoles().add(voterRole);
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public User block(UUID id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, ExceptionCodes.USER_NOT_FOUND));
        user.setBlocked(true);
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public User unblock(UUID id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, ExceptionCodes.USER_NOT_FOUND));
        user.setBlocked(false);
        return userRepository.save(user);
    }

    @PreAuthorize("isAuthenticated()")
    public User changePassword(ChangePasswordRequest password, UUID userId) throws NotFoundException, WrongPasswordException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, ExceptionCodes.USER_NOT_FOUND));
        if(!passwordEncoder.matches(password.oldPassword(), user.getPassword()))
            throw new WrongPasswordException(UserMessages.WRONG_PASSWORD, ExceptionCodes.WRONG_PASSWORD);

        user.setPassword(passwordEncoder.encode(password.newPassword()));
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public User getUser(String username) throws NotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, ExceptionCodes.USER_NOT_FOUND));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public UsersResponse getUsers(FilteringCriteria filteringCriteria) {
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
}
