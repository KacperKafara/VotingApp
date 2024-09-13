package pl.kafara.voting.users.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.handlers.ExceptionCodes;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.exceptions.user.VerificationTokenExpiredException;
import pl.kafara.voting.exceptions.user.VerificationTokenUsedException;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.users.tokens.SafeToken;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.util.SensitiveData;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class VerificationService {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {NotFoundException.class})
    public void verify(SensitiveData token) throws VerificationTokenUsedException, VerificationTokenExpiredException, NotFoundException {
        SafeToken accountVerificationToken = tokenService.validateAccountVerificationToken(token.data());
        User user = userRepository
                .findById(accountVerificationToken.getUser().getId())
                .orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, ExceptionCodes.USER_NOT_FOUND));
        user.setVerified(true);
        userRepository.save(user);
    }

}
