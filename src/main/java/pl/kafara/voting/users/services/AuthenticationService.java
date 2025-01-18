package pl.kafara.voting.users.services;

import com.auth0.jwt.interfaces.DecodedJWT;
import dev.samstevens.totp.code.CodeVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.exceptionCodes.UserExceptionCodes;
import pl.kafara.voting.exceptions.messages.GenericMessages;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.exceptions.user.AccountNotActiveException;
import pl.kafara.voting.exceptions.user.InvalidLoginDataException;
import pl.kafara.voting.exceptions.user.MFARequiredException;
import pl.kafara.voting.model.users.*;
import pl.kafara.voting.users.dto.LoginRequest;
import pl.kafara.voting.users.dto.RegistrationRequest;
import pl.kafara.voting.users.dto.TotpLoginRequest;
import pl.kafara.voting.users.mapper.RegistrationMapper;
import pl.kafara.voting.users.repositories.GenderRepository;
import pl.kafara.voting.users.repositories.RoleRepository;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.util.AESUtils;
import pl.kafara.voting.util.JwtService;
import pl.kafara.voting.util.SensitiveData;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

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
    private final AESUtils aesUtils;
    private final CodeVerifier codeVerifier;
    private final UserService userService;
    private final AuthenticationServiceUtils authenticationServiceUtils;

    @PreAuthorize("permitAll()")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Map<String, SensitiveData> authenticate(LoginRequest loginRequest) throws NotFoundException, AccountNotActiveException, InvalidLoginDataException, MFARequiredException {
        User user = authenticationServiceUtils.checkIsUserVerifiedOrBlocked(loginRequest.username());

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            user.setLastFailedLogin(LocalDateTime.now());
            userRepository.save(user);
            throw new InvalidLoginDataException(UserMessages.INVALID_CREDENTIALS, UserExceptionCodes.INVALID_CREDENTIALS);
        }

        if (user.getAuthorisationTotpSecret() != null) {
            user.setLanguage(loginRequest.language());
            userRepository.save(user);
            throw new MFARequiredException(UserMessages.MFA_REQUIRED, UserExceptionCodes.MFA_REQUIRED);
        }

        user.setLanguage(loginRequest.language());
        user.setFailedLoginAttempts(0);
        user.setLastLogin(LocalDateTime.now());
        User savedUser = userRepository.saveAndFlush(user);

        return authenticationServiceUtils.generateTokens(savedUser);
    }

    @PreAuthorize("permitAll()")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Map<String, SensitiveData> authenticate(SensitiveData subject) throws NotFoundException, AccountNotActiveException {
        User user = userService.getUserByOAuthId(subject.data());
        authenticationServiceUtils.checkIsUserVerifiedOrBlocked(user.getUsername());

        if (user.getAuthorisationTotpSecret() != null) {
            return Map.of(
                    "username", new SensitiveData(user.getUsername())
            );
        }

        user.setFailedLoginAttempts(0);
        user.setLastLogin(LocalDateTime.now());
        User savedUser = userRepository.saveAndFlush(user);
        return authenticationServiceUtils.generateTokens(savedUser);
    }

    @PreAuthorize("permitAll()")
    public Map<String, SensitiveData> verifyTotp(TotpLoginRequest loginRequest) throws NotFoundException, InvalidLoginDataException, AccountNotActiveException {
        User user = authenticationServiceUtils.checkIsUserVerifiedOrBlocked(loginRequest.username());

        if (!codeVerifier.isValidCode(aesUtils.decrypt(user.getAuthorisationTotpSecret()), loginRequest.totp())) {
            throw new InvalidLoginDataException(UserMessages.INVALID_CREDENTIALS, UserExceptionCodes.INVALID_CREDENTIALS);
        }

        user.setFailedLoginAttempts(0);
        user.setLastLogin(LocalDateTime.now());
        User savedUser = userRepository.saveAndFlush(user);
        return authenticationServiceUtils.generateTokens(savedUser);
    }

    @PreAuthorize("permitAll()")
    public SensitiveData register(RegistrationRequest registrationRequest) throws NotFoundException, NoSuchAlgorithmException {
        Role role = roleRepository.findByName(UserRoleEnum.USER)
                .orElseThrow(() -> new NotFoundException(UserMessages.ROLE_NOT_FOUND, UserExceptionCodes.ROLE_NOT_FOUND));
        Gender gender = genderRepository.findByName(GenderEnum.fromInt(registrationRequest.gender()))
                .orElseThrow(() -> new NotFoundException(GenericMessages.NOT_FOUND, UserExceptionCodes.NOT_FOUND));
        User user = RegistrationMapper.mapToUser(registrationRequest);
        user.setPassword(passwordEncoder.encode(registrationRequest.password()));
        user.getRoles().add(role);
        user.setGender(gender);
        userRepository.save(user);
        return tokenService.generateAccountVerificationToken(user);
    }

    @PreAuthorize("permitAll()")
    public SensitiveData register(User user, GenderEnum genderEnum) throws NotFoundException, NoSuchAlgorithmException {
        Role role = roleRepository.findByName(UserRoleEnum.USER)
            .orElseThrow(() -> new NotFoundException(UserMessages.ROLE_NOT_FOUND, UserExceptionCodes.ROLE_NOT_FOUND));
        Gender gender = genderRepository.findByName(genderEnum)
            .orElseThrow(() -> new NotFoundException(GenericMessages.NOT_FOUND, UserExceptionCodes.NOT_FOUND));

        user.getRoles().add(role);
        user.setGender(gender);
        userRepository.save(user);
        return tokenService.generateAccountVerificationToken(user);
    }

    @PreAuthorize("permitAll()")
    public Map<String, SensitiveData> refresh(SensitiveData refreshToken) throws NotFoundException {
        DecodedJWT decodedJWT = jwtService.decodeRefreshToken(refreshToken);
        UUID id = UUID.fromString(decodedJWT.getSubject());
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_NOT_FOUND));
        String jwtToken = jwtService.createToken(user.getUsername(), user.getId(), user.getRoles());

        return Map.of(
                "token", new SensitiveData(jwtToken),
                "refreshToken", refreshToken
        );
    }
}
