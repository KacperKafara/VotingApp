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
import pl.kafara.voting.model.users.*;
import pl.kafara.voting.users.dto.LoginRequest;
import pl.kafara.voting.users.dto.RegistrationRequest;
import pl.kafara.voting.users.mapper.RegistrationMapper;
import pl.kafara.voting.users.repositories.GenderRepository;
import pl.kafara.voting.users.repositories.RoleRepository;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.util.JwtService;
import pl.kafara.voting.util.SensitiveData;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AuthenticationService {

    private final GenderRepository genderRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final JwtService jwtService;

    @Value("${security.max-failed-attempts:3}")
    private int maxFailedAttempts;

    @PreAuthorize("permitAll()")
    public SensitiveData authenticate(LoginRequest loginRequest) throws NotFoundException, AccountNotActiveException, InvalidLoginDataException {
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, ExceptionCodes.INVALID_CREDENTIALS));

        if(!user.isVerified()) throw new AccountNotActiveException(UserMessages.USER_NOT_VERIFIED, ExceptionCodes.USER_NOT_VERIFIED);
        if(user.isBlocked()) throw new AccountNotActiveException(UserMessages.USER_BLOCKED, ExceptionCodes.USER_BLOCKED);

        if(user.getFailedLoginAttempts() >= maxFailedAttempts && Duration.between(user.getLastFailedLogin(), LocalDateTime.now()).toDays() <= 24)
            throw new AccountNotActiveException(UserMessages.AUTHENTICATION_BLOCKED, ExceptionCodes.AUTHENTICATION_BLOCKED);
        else if(user.getFailedLoginAttempts() >= maxFailedAttempts)
            user.setFailedLoginAttempts(0);

        if(!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            user.setLastFailedLogin(LocalDateTime.now());
            userRepository.save(user);
            throw new InvalidLoginDataException(UserMessages.INVALID_CREDENTIALS, ExceptionCodes.INVALID_CREDENTIALS);
        }

        user.setFailedLoginAttempts(0);
        user.setLastLogin(LocalDateTime.now());
        user.setLanguage(loginRequest.language());
        userRepository.save(user);

        return new SensitiveData(jwtService.createToken(user.getUsername(), user.getId(), user.getRoles()));
    }

    @PreAuthorize("permitAll()")
    public SensitiveData register(RegistrationRequest registrationRequest) throws NotFoundException, NoSuchAlgorithmException {
        Role role = roleRepository.findByName(UserRoleEnum.USER)
                .orElseThrow(() -> new NotFoundException(UserMessages.ROLE_NOT_FOUND, ExceptionCodes.ROLE_NOT_FOUND));
        Gender gender = genderRepository.findByName(GenderEnum.fromInt(registrationRequest.gender()))
                .orElseThrow(() -> new NotFoundException(GenericMessages.NOT_FOUND, ExceptionCodes.NOT_FOUND));
        User user = RegistrationMapper.mapToUser(registrationRequest);
        user.setPassword(passwordEncoder.encode(registrationRequest.password()));
        user.getRoles().add(role);
        user.setGender(gender);
        userRepository.save(user);
        return tokenService.generateAccountVerificationToken(user);
    }
}
