package pl.kafara.voting.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kafara.voting.exceptions.user.VerificationTokenExpiredException;
import pl.kafara.voting.exceptions.user.VerificationTokenUsedException;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.users.tokens.AccountVerificationToken;
import pl.kafara.voting.model.users.tokens.ResetPasswordToken;
import pl.kafara.voting.model.users.tokens.SafeToken;
import pl.kafara.voting.users.repositories.AccountVerificationTokenRepository;
import pl.kafara.voting.users.repositories.ResetPasswordTokenRepository;
import pl.kafara.voting.users.services.TokenService;
import pl.kafara.voting.util.SensitiveData;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
    @Mock
    AccountVerificationTokenRepository accountVerificationTokenRepository;
    @Mock
    ResetPasswordTokenRepository resetPasswordTokenRepository;
    @InjectMocks
    TokenService tokenService;

    @Test
    public void generateAccountVerificationToken_everythingOk_ShouldGenerateAndSaveToken() throws NoSuchAlgorithmException {
        User user = new User();
        SensitiveData token = new SensitiveData("generatedToken");

        when(accountVerificationTokenRepository.save(any(AccountVerificationToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SensitiveData result = tokenService.generateAccountVerificationToken(user);

        assertNotNull(result.data());
        verify(accountVerificationTokenRepository, times(1)).deleteByUserId(user.getId());
        verify(accountVerificationTokenRepository, times(1)).save(any(AccountVerificationToken.class));
    }

    @Test
    public void validateAccountVerificationToken_ValidToken_ShouldReturnToken() throws VerificationTokenUsedException, VerificationTokenExpiredException {
        String token = "validToken";
        AccountVerificationToken accountVerificationToken = new AccountVerificationToken(token, Instant.now().plusSeconds(60), new User());

        when(accountVerificationTokenRepository.findByToken(token)).thenReturn(Optional.of(accountVerificationToken));

        SafeToken result = tokenService.validateAccountVerificationToken(token);

        assertEquals(accountVerificationToken, result);
        verify(accountVerificationTokenRepository, times(1)).deleteById(accountVerificationToken.getId());
    }

    @Test
    public void validateAccountVerificationToken_ExpiredToken_ShouldThrowVerificationTokenExpiredException() {
        String token = "expiredToken";
        AccountVerificationToken accountVerificationToken = new AccountVerificationToken(token, Instant.now().minusSeconds(60), new User());

        when(accountVerificationTokenRepository.findByToken(token)).thenReturn(Optional.of(accountVerificationToken));

        assertThrows(VerificationTokenExpiredException.class, () -> tokenService.validateAccountVerificationToken(token));
    }

    @Test
    public void validateAccountVerificationToken_UsedToken_ShouldThrowVerificationTokenUsedException() {
        String token = "usedToken";

        when(accountVerificationTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        assertThrows(VerificationTokenUsedException.class, () -> tokenService.validateAccountVerificationToken(token));
    }

    @Test
    public void generateResetPasswordToken_ShouldGenerateAndSaveToken() throws NoSuchAlgorithmException {
        User user = new User();
        SensitiveData token = new SensitiveData("generatedToken");

        when(resetPasswordTokenRepository.save(any(ResetPasswordToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SensitiveData result = tokenService.generateResetPasswordToken(user);

        assertNotNull(result.data());
        verify(resetPasswordTokenRepository, times(1)).deleteByUserId(user.getId());
        verify(resetPasswordTokenRepository, times(1)).save(any(ResetPasswordToken.class));
    }

    @Test
    public void validateResetPasswordToken_ValidToken_ShouldReturnToken() throws VerificationTokenUsedException, VerificationTokenExpiredException {
        String token = "validToken";
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(token, Instant.now().plusSeconds(60), new User());

        when(resetPasswordTokenRepository.findByToken(token)).thenReturn(Optional.of(resetPasswordToken));

        SafeToken result = tokenService.validateResetPasswordToken(token);

        assertEquals(resetPasswordToken, result);
        verify(resetPasswordTokenRepository, times(1)).deleteById(resetPasswordToken.getId());
    }

    @Test
    public void validateResetPasswordToken_ExpiredToken_ShouldThrowVerificationTokenExpiredException() {
        String token = "expiredToken";
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(token, Instant.now().minusSeconds(60), new User());

        when(resetPasswordTokenRepository.findByToken(token)).thenReturn(Optional.of(resetPasswordToken));

        assertThrows(VerificationTokenExpiredException.class, () -> tokenService.validateResetPasswordToken(token));
    }

    @Test
    public void validateResetPasswordToken_UsedToken_ShouldThrowVerificationTokenUsedException() {
        String token = "usedToken";

        when(resetPasswordTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        assertThrows(VerificationTokenUsedException.class, () -> tokenService.validateResetPasswordToken(token));
    }

    @Test
    public void isResetPasswordTokenValid_ValidToken_ShouldReturnTrue() {
        String token = "validToken";

        when(resetPasswordTokenRepository.findByToken(token)).thenReturn(Optional.of(new ResetPasswordToken()));

        boolean result = tokenService.isResetPasswordTokenValid(token);

        assertTrue(result);
    }

    @Test
    public void isResetPasswordTokenValid_InvalidToken_ShouldReturnFalse() {
        String token = "invalidToken";

        when(resetPasswordTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        boolean result = tokenService.isResetPasswordTokenValid(token);

        assertFalse(result);
    }
}
