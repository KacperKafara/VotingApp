package pl.kafara.voting.users.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.handlers.ExceptionCodes;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.exceptions.user.VerificationTokenExpiredException;
import pl.kafara.voting.exceptions.user.VerificationTokenUsedException;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.users.tokens.AccountVerificationToken;
import pl.kafara.voting.model.users.tokens.SafeToken;
import pl.kafara.voting.users.repositories.AccountVerificationTokenRepository;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class TokenService {

    private final AccountVerificationTokenRepository accountVerificationTokenRepository;

    public String generateAccountVerificationToken(User user) throws NoSuchAlgorithmException {
        String token = generateSafeToken();
        accountVerificationTokenRepository.deleteByUserId(user.getId());
        AccountVerificationToken accountVerificationToken = new AccountVerificationToken(token, Instant.now().plus(AccountVerificationToken.EXPIRATION_TIME, ChronoUnit.MINUTES), user);
        return accountVerificationTokenRepository.save(accountVerificationToken).getToken();
    }

    public SafeToken validateAccountVerificationToken(String token) throws VerificationTokenUsedException, VerificationTokenExpiredException {
        SafeToken accountVerificationToken = accountVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new VerificationTokenUsedException(UserMessages.ACCOUNT_VERIFICATION_TOKEN_USED, ExceptionCodes.ACCOUNT_VERIFICATION_TOKEN_USED));

        if (accountVerificationToken.getExpirationDate().isBefore(Instant.now()))
            throw new VerificationTokenExpiredException(UserMessages.ACCOUNT_VERIFICATION_TOKEN_EXPIRED, ExceptionCodes.ACCOUNT_VERIFICATION_TOKEN_EXPIRED);

        accountVerificationTokenRepository.deleteById(accountVerificationToken.getId());
        return accountVerificationToken;
    }

    private String generateSafeToken() throws NoSuchAlgorithmException {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyz-_ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom random = SecureRandom.getInstanceStrong();
        return random.ints(32, 0, chars.length())
                .mapToObj(chars::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
