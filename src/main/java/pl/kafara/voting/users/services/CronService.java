package pl.kafara.voting.users.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.users.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class CronService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 13 */2 * * *")
    public void deleteNotVerifiedAccounts() {
        List<User> users = userRepository.getUsersByCreatedAtBeforeAndVerifiedIsFalse(LocalDateTime.now().minusDays(1));
        emailService.sendAccountDeletedEmail(users);
        userRepository.deleteAll(users);
    }

    @Scheduled(cron = "0 0 * * * *", zone = "UTC")
    public void unblockUsers() {
        List<User>  users = userRepository.getUsersByLastFailedLoginBeforeAndBlockedIsTrue(LocalDateTime.now().minusDays(1));
        users.forEach(user -> {
            user.setBlocked(false);
            user.setFailedLoginAttempts(0);
        });
        userRepository.saveAll(users);
    }
}
