package pl.kafara.voting.users.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.model.users.tokens.AccountVerificationToken;
import pl.kafara.voting.users.repositories.AccountVerificationTokenRepository;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.util.SensitiveData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class CronService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final AccountVerificationTokenRepository accountVerificationTokenRepository;

    private final int removeAccountsAfter = 24;

    @Scheduled(cron = "0 13 */2 * * *")
    public void deleteNotVerifiedAccounts() {
        List<User> users = userRepository.getUsersByCreatedAtBeforeAndVerifiedIsFalse(LocalDateTime.now().minusHours(removeAccountsAfter));
        emailService.sendAccountDeletedEmail(users);
        userRepository.deleteAll(users);
    }

    @Scheduled(cron = "0 0 */2 * * *")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public void sendVerificationReminder() {
        LocalDateTime createdAtBefore = LocalDateTime.now().minusHours(removeAccountsAfter / 2);
        LocalDateTime createdAtAfter = createdAtBefore.plusHours(2);
        List<User> users = userRepository.getUsersByCreatedAtBeforeAndCreatedAtAfterAndVerifiedIsFalse(createdAtBefore, createdAtAfter);
        for(User user : users) {
            Optional<AccountVerificationToken> token = accountVerificationTokenRepository.findByUserId(user.getId());
            if (token.isEmpty())
                continue;
            emailService.sendVerificationReminderEmail(user, new SensitiveData(token.get().getToken()));
        }
    }
}
