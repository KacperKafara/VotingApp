package pl.kafara.voting.users.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.handlers.ExceptionCodes;
import pl.kafara.voting.exceptions.messages.GenericMessages;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.exceptions.user.AccountNotActiveException;
import pl.kafara.voting.exceptions.user.InvalidLoginDataException;
import pl.kafara.voting.model.users.Role;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.users.UserRoleEnum;
import pl.kafara.voting.users.dto.LoginRequest;
import pl.kafara.voting.users.dto.RegistrationRequest;
import pl.kafara.voting.users.mapper.RegistrationMapper;
import pl.kafara.voting.users.repositories.RoleRepository;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.util.JwtService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${security.max-failed-attempts:3}")
    private int maxFailedAttempts;

    @PreAuthorize("permitAll()")
    public String authenticate(LoginRequest loginRequest) throws NotFoundException, AccountNotActiveException, InvalidLoginDataException {
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, ExceptionCodes.USER_NOT_FOUND));

        if(!user.isVerified()) throw new AccountNotActiveException(UserMessages.USER_NOT_VERIFIED, ExceptionCodes.USER_NOT_FOUND);
        if(user.isBlocked()) throw new AccountNotActiveException(UserMessages.USER_BLOCKED, ExceptionCodes.USER_BLOCKED);

        if(!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if(user.getFailedLoginAttempts() >= maxFailedAttempts) {
                user.setBlocked(true);
            }
            userRepository.save(user);
            throw new InvalidLoginDataException(UserMessages.INVALID_CREDENTIALS, ExceptionCodes.INVALID_CREDENTIALS);
        }

        user.setFailedLoginAttempts(0);
        userRepository.save(user);

        return jwtService.createToken(user.getUsername(), user.getId(), user.getRoles());
    }

    @PreAuthorize("permitAll()")
    public User register(RegistrationRequest registrationRequest) throws NotFoundException {
        Role role = roleRepository.findByName(UserRoleEnum.USER)
                .orElseThrow(() -> new NotFoundException(UserMessages.ROLE_NOT_FOUND, ExceptionCodes.ROLE_NOT_FOUND));
        User user = RegistrationMapper.mapToUser(registrationRequest);
        user.setPassword(passwordEncoder.encode(registrationRequest.password()));
        user.getRoles().add(role);
        return userRepository.save(user);
    }
}
