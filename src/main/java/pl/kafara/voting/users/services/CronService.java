package pl.kafara.voting.users.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.users.tokens.AccountVerificationToken;
import pl.kafara.voting.users.repositories.AccountVerificationTokenRepository;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.util.SensitiveData;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class CronService {

    private final UserRepository userRepository;
    private final AccountVerificationTokenRepository accountVerificationTokenRepository;

    private final int removeAccountsAfter = 24;

    public List<User> deleteNotVerifiedAccounts() {
        List<User> users = userRepository.getUsersByCreatedAtBeforeAndVerifiedIsFalse(LocalDateTime.now().minusHours(removeAccountsAfter));
        userRepository.deleteAll(users);
        return users;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Map<SensitiveData, User> sendVerificationReminder() {
        Map<SensitiveData, User> usersToSendEmail = new HashMap<>();
        LocalDateTime createdAtBefore = LocalDateTime.now().minusHours(removeAccountsAfter / 2);
        LocalDateTime createdAtAfter = createdAtBefore.plusHours(2);
        List<User> users = userRepository.getUsersByCreatedAtBeforeAndCreatedAtAfterAndVerifiedIsFalse(createdAtBefore, createdAtAfter);
        for(User user : users) {
            Optional<AccountVerificationToken> token = accountVerificationTokenRepository.findByUserId(user.getId());
            if (token.isEmpty())
                continue;
            usersToSendEmail.put(new SensitiveData(token.get().getToken()), user);
        }
        return usersToSendEmail;
    }
}
