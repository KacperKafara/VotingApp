package pl.kafara.voting.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.model.users.*;
import pl.kafara.voting.users.dto.RegistrationRequest;
import pl.kafara.voting.users.repositories.GenderRepository;
import pl.kafara.voting.users.repositories.RoleRepository;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.users.services.AuthenticationService;
import pl.kafara.voting.users.services.TokenService;
import pl.kafara.voting.util.JwtService;
import pl.kafara.voting.util.SensitiveData;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        user.setRoles(List.of(role));
        when(roleRepository.findByName(UserRoleEnum.USER)).thenReturn(Optional.of(role));
        when(genderRepository.findByName(GenderEnum.fromInt(registrationRequest.gender()))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            authenticationService.register(registrationRequest);
        });
    }
}
