package pl.kafara.voting.users.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.handlers.ExceptionCodes;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.exceptions.user.AccountNotActiveException;
import pl.kafara.voting.exceptions.user.UserMustHaveAtLeastOneRoleException;
import pl.kafara.voting.exceptions.user.VerificationTokenExpiredException;
import pl.kafara.voting.exceptions.user.VerificationTokenUsedException;
import pl.kafara.voting.model.users.Role;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.users.UserRoleEnum;
import pl.kafara.voting.model.users.tokens.SafeToken;
import pl.kafara.voting.users.dto.ResetPasswordFormRequest;
import pl.kafara.voting.users.repositories.RoleRepository;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.util.SensitiveData;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

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
    public void addRole(UUID id, String role) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, ExceptionCodes.USER_NOT_FOUND));
        Role roleEntity = roleRepository.findByName(UserRoleEnum.fromString(role)).orElseThrow(() -> new NotFoundException(UserMessages.ROLE_NOT_FOUND, ExceptionCodes.ROLE_NOT_FOUND));

        user.getRoles().add(roleEntity);
        userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void removeRole(UUID id, String role) throws NotFoundException, UserMustHaveAtLeastOneRoleException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, ExceptionCodes.USER_NOT_FOUND));
        Role roleEntity = roleRepository.findByName(UserRoleEnum.fromString(role)).orElseThrow(() -> new NotFoundException(UserMessages.ROLE_NOT_FOUND, ExceptionCodes.ROLE_NOT_FOUND));

        if(user.getRoles().size() == 1) {
            throw new UserMustHaveAtLeastOneRoleException(UserMessages.USER_MUST_HAVE_AT_LEAST_ONE_ROLE, ExceptionCodes.USER_MUST_HAVE_AT_LEAST_ONE_ROLE);
        }

        user.getRoles().remove(roleEntity);
        userRepository.save(user);
    }
}
