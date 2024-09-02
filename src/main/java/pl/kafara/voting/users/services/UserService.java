package pl.kafara.voting.users.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.handlers.ExceptionCodes;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.exceptions.user.AccountNotActiveException;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.users.repositories.UserRepository;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserService {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public String resetPassword(String email) throws NotFoundException, AccountNotActiveException, NoSuchAlgorithmException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(UserMessages.USER_NOT_FOUND, ExceptionCodes.USER_WITH_EMAIL_NOT_FOUND)
        );

        if(user.isBlocked())
            throw new AccountNotActiveException(UserMessages.USER_BLOCKED, ExceptionCodes.USER_BLOCKED);

        if(user.isVerified())
            throw new AccountNotActiveException(UserMessages.USER_NOT_VERIFIED, ExceptionCodes.USER_NOT_VERIFIED);

        return tokenService.generateResetPasswordToken(user);
    }
}
