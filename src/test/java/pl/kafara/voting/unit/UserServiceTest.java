package pl.kafara.voting.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.user.AccountNotActiveException;
import pl.kafara.voting.exceptions.user.VerificationTokenExpiredException;
import pl.kafara.voting.exceptions.user.VerificationTokenUsedException;
import pl.kafara.voting.exceptions.user.WrongPasswordException;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.users.UserRoleEnum;
import pl.kafara.voting.model.users.tokens.ResetPasswordToken;
import pl.kafara.voting.users.dto.ChangePasswordRequest;
import pl.kafara.voting.users.dto.ResetPasswordFormRequest;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.users.services.TokenService;
import pl.kafara.voting.users.services.UserService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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

    @Test
    public void ResetPassword_WithValidToken_ShouldResetPassword() throws NotFoundException, VerificationTokenUsedException, VerificationTokenExpiredException {
        ResetPasswordToken safeToken = new ResetPasswordToken(
                token,
                null,
                user
        );
        when(tokenService.validateResetPasswordToken(token)).thenReturn(safeToken);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(password.password())).thenReturn("encodedPassword");

        assertDoesNotThrow(() -> {
            userService.resetPassword(token, password);
        });
    }

    @Test
    public void ResetPassword_WithInvalidToken_ShouldThrowVerificationTokenExpiredException() throws VerificationTokenUsedException, VerificationTokenExpiredException {
        when(tokenService.validateResetPasswordToken(token)).thenThrow(new VerificationTokenExpiredException("Token expired"));

        assertThrows(VerificationTokenExpiredException.class, () -> {
            userService.resetPassword(token, password);
        });
    }

    @Test
    public void ModifyUserRoles_WhenUserNotFound_ShouldThrowNotFoundException() {
        UUID userId = UUID.randomUUID();
        Set<UserRoleEnum> roles = new HashSet<>();
        roles.add(UserRoleEnum.USER);
        String tagValue = "tagValue";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.modifyUserRoles(userId, roles, tagValue);
        });
    }

    @Test
    public void BlockUser_WhenUserNotFound_ShouldThrowNotFoundException() {
        UUID userId = UUID.randomUUID();
        String tagValue = "tagValue";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.block(userId, tagValue);
        });
    }

    @Test
    public void UnblockUser_WhenUserNotFound_ShouldThrowNotFoundException() {
        UUID userId = UUID.randomUUID();
        String tagValue = "tagValue";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.unblock(userId, tagValue);
        });
    }

    @Test
    public void ChangePassword_WhenOldPasswordDoesNotMatch_ShouldThrowWrongPasswordException() {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("oldPassword", "newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())).thenReturn(false);

        assertThrows(WrongPasswordException.class, () -> {
            userService.changePassword(changePasswordRequest, userId);
        });
    }

    @Test
    public void ChangePassword_WhenOldPasswordMatches_ShouldChangePassword() {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("oldPassword", "newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(changePasswordRequest.newPassword())).thenReturn("encodedNewPassword");

        assertDoesNotThrow(() -> {
            userService.changePassword(changePasswordRequest, userId);
        });
    }
}
