package pl.kafara.voting.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.users.services.CronService;
import pl.kafara.voting.users.services.EmailService;
import pl.kafara.voting.util.SensitiveData;
import pl.kafara.voting.vote.api.services.ApiService;

import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.NEVER)
public class CronServiceExecutor {
    private final CronService cronService;
    private final EmailService emailService;
    private final ApiService apiService;

    @Autowired
    public CronServiceExecutor(CronService cronService, EmailService emailService, @Nullable ApiService apiService) {
        this.cronService = cronService;
        this.emailService = emailService;
        this.apiService = apiService;
    }

    @PostConstruct
    public void init() {
        if (apiService == null) return;
        apiService.updateParliamentaryClubList();
        apiService.updateEnvoyList();
        apiService.updateSittingList();
        apiService.updateVotingList();
    }

    @Scheduled(cron = "0 13 */2 * * *")
    public void executeDeleteNotVerifiedAccounts() {
        List<User> users = cronService.deleteNotVerifiedAccounts();
        emailService.sendAccountDeletedEmail(users);
    }

    @Scheduled(cron = "0 0 */2 * * *")
    public void executeSendVerificationReminder() {
        Map<SensitiveData, User> result = cronService.sendVerificationReminder();
        for (Map.Entry<SensitiveData, User> entry : result.entrySet()) {
            emailService.sendVerificationReminderEmail(entry.getValue(), entry.getKey());
        }
    }

    @Scheduled(cron = "* * * * * *")
    public void executeUpdateParliamentaryClubList() {
        if (apiService == null) return;
        apiService.updateParliamentaryClubList();
    }

    @Scheduled(cron = "0 10 0 */2 * *")
    public void executeUpdateEnvoyList() {
        if (apiService == null) return;
        apiService.updateEnvoyList();
    }

    @Scheduled(cron = "0 15 0 */2 * *")
    public void executeUpdateSittingList() {
        if (apiService == null) return;
        apiService.updateSittingList();
    }

    @Scheduled(cron = "0 20 0 */2 * *")
    public void executeUpdateVotingList() {
        if (apiService == null) return;
        apiService.updateVotingList();
    }
}
