package pl.kafara.voting.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.user.VerificationTokenExpiredException;
import pl.kafara.voting.exceptions.user.VerificationTokenUsedException;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.users.tokens.AccountVerificationToken;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.users.services.TokenService;
import pl.kafara.voting.users.services.VerificationService;
import pl.kafara.voting.util.SensitiveData;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerificationServiceTest {

    @Mock
    TokenService tokenService;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    VerificationService verificationService;

    @Test
    public void verify_UserNotFound_ShouldThrowNotFoundException() throws VerificationTokenUsedException, VerificationTokenExpiredException, NoSuchFieldException, IllegalAccessException {
        String token = "sampleToken";
        UUID userId = UUID.randomUUID();
        User user = new User();
        Field ifField = user.getClass().getSuperclass().getDeclaredField("id");
        ifField.setAccessible(true);
        ifField.set(user, userId);
        AccountVerificationToken accountVerificationToken = new AccountVerificationToken(
                token,
                Instant.now().plusSeconds(60),
                user
        );

        when(tokenService.validateAccountVerificationToken(token)).thenReturn(accountVerificationToken);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> verificationService.verify(new SensitiveData(token)));
    }

    @Test
    public void verify_TokenExpired_ShouldThrowVerificationTokenExpiredException() throws VerificationTokenUsedException, VerificationTokenExpiredException {
        String token = "sampleToken";

        when(tokenService.validateAccountVerificationToken(token)).thenThrow(VerificationTokenExpiredException.class);

        assertThrows(VerificationTokenExpiredException.class, () -> verificationService.verify(new SensitiveData(token)));
    }

    @Test
    public void verify_TokenUsed_ShouldThrowVerificationTokenUsedException() throws VerificationTokenUsedException, VerificationTokenExpiredException {
        String token = "sampleToken";

        when(tokenService.validateAccountVerificationToken(token)).thenThrow(VerificationTokenUsedException.class);

        assertThrows(VerificationTokenUsedException.class, () -> verificationService.verify(new SensitiveData(token)));
    }

    @Test
    public void verify_ValidToken_ShouldVerifyUser() throws Exception {
        String token = "sampleToken";
        UUID userId = UUID.randomUUID();
        User user = new User();
        Field ifField = user.getClass().getSuperclass().getDeclaredField("id");
        ifField.setAccessible(true);
        ifField.set(user, userId);
        AccountVerificationToken accountVerificationToken = new AccountVerificationToken(
                token,
                Instant.now().plusSeconds(60),
                user
        );

        when(tokenService.validateAccountVerificationToken(token)).thenReturn(accountVerificationToken);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        verificationService.verify(new SensitiveData(token));

        assertTrue(user.isVerified());
        verify(userRepository, times(1)).save(user);
    }
}
