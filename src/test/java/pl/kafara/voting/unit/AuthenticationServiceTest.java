package pl.kafara.voting.unit;

import com.auth0.jwt.interfaces.DecodedJWT;
import dev.samstevens.totp.code.CodeVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.exceptionCodes.UserExceptionCodes;
import pl.kafara.voting.exceptions.user.AccountNotActiveException;
import pl.kafara.voting.exceptions.user.InvalidLoginDataException;
import pl.kafara.voting.exceptions.user.MFARequiredException;
import pl.kafara.voting.model.users.*;
import pl.kafara.voting.users.dto.LoginRequest;
import pl.kafara.voting.users.dto.RegistrationRequest;
import pl.kafara.voting.users.dto.TotpLoginRequest;
import pl.kafara.voting.users.repositories.GenderRepository;
import pl.kafara.voting.users.repositories.RoleRepository;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.users.services.AuthenticationService;
import pl.kafara.voting.users.services.AuthenticationServiceUtils;
import pl.kafara.voting.users.services.TokenService;
import pl.kafara.voting.users.services.UserService;
import pl.kafara.voting.util.AESUtils;
import pl.kafara.voting.util.JwtService;
import pl.kafara.voting.util.SensitiveData;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    GenderRepository genderRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserRepository userRepository;
    @Mock
    TokenService tokenService;
    @Mock
    JwtService jwtService;
    @Mock
    AESUtils aesUtils;
    @Mock
    CodeVerifier codeVerifier;
    @Mock
    UserService userService;
    @Mock
    AuthenticationServiceUtils authenticationServiceUtils;
    @InjectMocks
    AuthenticationService authenticationService;

    RegistrationRequest registrationRequest = new RegistrationRequest("username", "email", "password", "password", "password", "password", LocalDateTime.now(), 0, "pl");

    @Test
    public void Register_RoleNotFound_ShouldThrowNotFoundException() {
        when(roleRepository.findByName(UserRoleEnum.USER)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            authenticationService.register(registrationRequest);
        });
    }

    @Test
    public void Register_GenderNotFound_ShouldThrowNotFoundException() {
        User user = new User();
        Role role = new Role(UserRoleEnum.USER);
        user.setRoles(Set.of(role));
        when(roleRepository.findByName(UserRoleEnum.USER)).thenReturn(Optional.of(role));
        when(genderRepository.findByName(GenderEnum.fromInt(registrationRequest.gender()))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            authenticationService.register(registrationRequest);
        });
    }

    @Test
    public void Register_Success_ShouldReturnSensitiveData() throws NotFoundException, NoSuchAlgorithmException, NoSuchAlgorithmException {
        Role role = new Role(UserRoleEnum.USER);
        Gender gender = new Gender(GenderEnum.MALE);
        User user = new User();
        user.setRoles(Set.of(role));
        user.setGender(gender);

        when(roleRepository.findByName(UserRoleEnum.USER)).thenReturn(Optional.of(role));
        when(genderRepository.findByName(GenderEnum.fromInt(registrationRequest.gender()))).thenReturn(Optional.of(gender));
        when(passwordEncoder.encode(registrationRequest.password())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(tokenService.generateAccountVerificationToken(user)).thenReturn(new SensitiveData("token"));

        SensitiveData result = authenticationService.register(registrationRequest);

        assertNotNull(result);
        assertEquals("token", result.data());
    }

    @Test
    public void Authenticate_InvalidPassword_ShouldThrowInvalidLoginDataException() throws NotFoundException, AccountNotActiveException {
        User user = new User();
        user.setUsername("username");
        user.setPassword("encodedPassword");
        user.setFailedLoginAttempts(-1);
        user.setVerified(true);
        user.setBlocked(false);
        user.setLastFailedLogin(LocalDateTime.now());

        when(authenticationServiceUtils.checkIsUserVerifiedOrBlocked(user.getUsername())).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        LoginRequest loginRequest = new LoginRequest("username", "wrongPassword", "en");

        assertThrows(InvalidLoginDataException.class, () -> {
            authenticationService.authenticate(loginRequest);
        });
    }

    @Test
    public void Authenticate_Success_ShouldReturnTokens() throws NotFoundException, AccountNotActiveException, InvalidLoginDataException, MFARequiredException {
        User user = new User();
        user.setUsername("username");
        user.setPassword("encodedPassword");
        user.setFailedLoginAttempts(-1);
        user.setBlocked(false);
        user.setVerified(true);
        user.setLastFailedLogin(LocalDateTime.now());
        user.setRoles(Set.of(new Role(UserRoleEnum.USER)));

        when(authenticationServiceUtils.checkIsUserVerifiedOrBlocked(user.getUsername())).thenReturn(user);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(authenticationServiceUtils.generateTokens(any())).thenReturn(Map.of(
                "token", new SensitiveData("jwtToken"),
                "refreshToken", new SensitiveData("refreshToken"),
                "etag", new SensitiveData("etag")
        ));

        LoginRequest loginRequest = new LoginRequest("username", "password", "en");

        Map<String, SensitiveData> result = authenticationService.authenticate(loginRequest);

        assertNotNull(result);
        assertEquals("jwtToken", result.get("token").data());
        assertEquals("refreshToken", result.get("refreshToken").data());
    }

    @Test
    public void VerifyTotp_InvalidCode_ShouldThrowInvalidLoginDataException() throws NotFoundException, AccountNotActiveException {
        User user = new User();
        user.setUsername("username");
        user.setAuthorisationTotpSecret("encryptedSecret");
        user.setFailedLoginAttempts(0);
        user.setBlocked(false);
        user.setVerified(true);

        when(authenticationServiceUtils.checkIsUserVerifiedOrBlocked(user.getUsername())).thenReturn(user);

        TotpLoginRequest loginRequest = new TotpLoginRequest("username", "invalidTotp");

        assertThrows(InvalidLoginDataException.class, () -> {
            authenticationService.verifyTotp(loginRequest);
        });
    }

    @Test
    public void VerifyTotp_Success_ShouldReturnTokens() throws NotFoundException, InvalidLoginDataException, AccountNotActiveException {
        User user = new User();
        user.setUsername("username");
        user.setAuthorisationTotpSecret("encryptedSecret");
        user.setFailedLoginAttempts(0);
        user.setBlocked(false);
        user.setVerified(true);
        user.setRoles(Set.of(new Role(UserRoleEnum.USER)));

        when(authenticationServiceUtils.checkIsUserVerifiedOrBlocked(user.getUsername())).thenReturn(user);
        when(aesUtils.decrypt("encryptedSecret")).thenReturn("decryptedSecret");
        when(codeVerifier.isValidCode("decryptedSecret", "validTotp")).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);
        when(authenticationServiceUtils.generateTokens(any())).thenReturn(Map.of(
                "token", new SensitiveData("jwtToken"),
                "refreshToken", new SensitiveData("refreshToken"),
                "etag", new SensitiveData("etag")
        ));

        TotpLoginRequest loginRequest = new TotpLoginRequest("username", "validTotp");
        Map<String, SensitiveData> result = authenticationService.verifyTotp(loginRequest);

        assertNotNull(result);
        assertEquals("jwtToken", result.get("token").data());
        assertEquals("refreshToken", result.get("refreshToken").data());
    }

    @Test
    public void Refresh_Success_ShouldReturnTokens() throws NotFoundException {
        User user = new User();
        user.setUsername("username");
        user.setRoles(Set.of(new Role(UserRoleEnum.USER)));

        UUID userId = UUID.randomUUID();
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getSubject()).thenReturn(userId.toString());
        when(jwtService.decodeRefreshToken(new SensitiveData("validToken"))).thenReturn(decodedJWT);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(jwtService.createToken("username", user.getId(), user.getRoles())).thenReturn("jwtToken");

        SensitiveData refreshToken = new SensitiveData("validToken");

        Map<String, SensitiveData> result = authenticationService.refresh(refreshToken);

        assertNotNull(result);
        assertEquals("jwtToken", result.get("token").data());
        assertEquals("validToken", result.get("refreshToken").data());
    }
}
