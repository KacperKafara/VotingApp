package pl.kafara.voting.users.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.exceptionCodes.UserExceptionCodes;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.exceptions.user.AccountNotActiveException;
import pl.kafara.voting.model.users.Role;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.users.UserRoleEnum;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.util.JwsService;
import pl.kafara.voting.util.JwtService;
import pl.kafara.voting.util.SensitiveData;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationServiceUtils {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JwsService jwsService;

    @Value("${security.max-failed-attempts:3}")
    private int maxFailedAttempts;

    @Transactional(propagation = Propagation.REQUIRED)
    public User checkIsUserVerifiedOrBlocked(String username) throws NotFoundException, AccountNotActiveException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.INVALID_CREDENTIALS));

        if (!user.isVerified())
            throw new AccountNotActiveException(UserMessages.USER_NOT_VERIFIED, UserExceptionCodes.USER_NOT_VERIFIED);
        if (user.isBlocked())
            throw new AccountNotActiveException(UserMessages.USER_BLOCKED, UserExceptionCodes.USER_BLOCKED);

        if (user.getLastFailedLogin() == null)
            return user;

        if (user.getFailedLoginAttempts() >= maxFailedAttempts && Duration.between(user.getLastFailedLogin(), LocalDateTime.now()).toDays() <= 24)
            throw new AccountNotActiveException(UserMessages.AUTHENTICATION_BLOCKED, UserExceptionCodes.AUTHENTICATION_BLOCKED);
        else if (user.getFailedLoginAttempts() >= maxFailedAttempts)
            user.setFailedLoginAttempts(0);

        return userRepository.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, SensitiveData> generateTokens(User user) {
        String jwtToken = jwtService.createToken(user.getUsername(), user.getId(), user.getRoles());
        String refreshToken = jwtService.createRefreshToken(user.getId());
        boolean isVoter = user.getRoles().stream().anyMatch(role -> role.getName().equals(UserRoleEnum.VOTER));

        String tagValue = null;
        if (user.getParliamentaryClub() == null && isVoter) {
            tagValue = jwsService.createToken(user.getId(), user.getVersion());
        }

        return Map.of(
                "token", new SensitiveData(jwtToken),
                "refreshToken", new SensitiveData(refreshToken),
                "etag", new SensitiveData(tagValue)
        );
    }
}
