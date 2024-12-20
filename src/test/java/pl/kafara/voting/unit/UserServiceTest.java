package pl.kafara.voting.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.user.AccountNotActiveException;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.users.dto.ResetPasswordFormRequest;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.users.services.TokenService;
import pl.kafara.voting.users.services.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private TokenService tokenService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    String email = "example@email.com";
    String token = "token";
    ResetPasswordFormRequest password = new ResetPasswordFormRequest("password");
    User user = new User();

    @Test
    public void ResetPassword_WhenUserNotFound_ShouldThrowNotFoundException() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            userService.resetPassword(email);
        });
    }

    @Test
    public void ResetPassword_WhenUserIsBlocked_ShouldThrowAccountNotActiveException() {
        User user = new User();
        user.setBlocked(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThrows(AccountNotActiveException.class, () -> {
            userService.resetPassword(email);
        });
    }

    @Test
    public void ResetPassword_WhenUserIsNotVerified_ShouldThrowAccountNotActiveException() {
        User user = new User();
        user.setBlocked(false);
        user.setVerified(false);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThrows(AccountNotActiveException.class, () -> {
            userService.resetPassword(email);
        });
    }

    @Test
    public void ResetPassword_WhenUserIsNotBlockedAndIsVerified_ShouldNotThrowAnyException() {
        User user = new User();
        user.setBlocked(false);
        user.setVerified(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> {
            userService.resetPassword(email);
        });
    }
}
