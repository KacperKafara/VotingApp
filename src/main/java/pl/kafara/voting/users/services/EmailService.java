package pl.kafara.voting.users.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.services.HtmlEmailService;
import pl.kafara.voting.util.SensitiveData;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class EmailService {
    private final HtmlEmailService htmlEmailService;
    private final ResourceBundleMessageSource mailMessageSource;

    @Value("${app.url}")
    private String url;

    public void sendAccountVerificationEmail(String to, SensitiveData token, String name, String lang) {
        URI uri = URI.create(url + "/verify/" + token.data());
        Map<String, Object> templateModel = Map.of(
                "name", name,
                "uri", uri
        );
        String subject = mailMessageSource.getMessage("accountVerification.subject", null, Locale.of(lang));
        htmlEmailService.createHtmlEmail(to, subject, "accountVerification", templateModel, lang);
    }

    public void sendResetPasswordEmail(String email, SensitiveData token, String lang) {
        URI uri = URI.create(url + "/resetPassword/" + token.data());
        Map<String, Object> templateModel = Map.of(
                "uri", uri
        );
        String subject = mailMessageSource.getMessage("resetPassword.subject", null, Locale.of(lang));
        htmlEmailService.createHtmlEmail(email, subject, "resetPassword", templateModel, lang);
    }

    @Async
    public void sendAccountDeletedEmail(List<User> users) {
        for (User user : users) {
            Map<String, Object> templateModel = Map.of(
                    "name", user.getUsername()
            );
            String subject = mailMessageSource.getMessage("accountDeleted.subject", null, Locale.of(user.getLanguage()));
            htmlEmailService.createHtmlEmail(user.getEmail(), subject, "accountDeleted", templateModel, user.getLanguage());
        }
    }

    public void sendAccountBlockedEmail(String email, String username, String language) {
        Map<String, Object> templateModel = Map.of(
                "name", username
        );
        String subject = mailMessageSource.getMessage("accountBlocked.subject", null, Locale.of(language));
        htmlEmailService.createHtmlEmail(email, subject, "accountBlocked", templateModel, language);
    }

    public void sendAccountUnblockedEmail(String email, String username, String language) {
        Map<String, Object> templateModel = Map.of(
                "name", username
        );
        String subject = mailMessageSource.getMessage("accountUnblocked.subject", null, Locale.of(language));
        htmlEmailService.createHtmlEmail(email, subject, "accountUnblocked", templateModel, language);
    }

    public void sendVoterRoleRequestAcceptedEmail(String email, String username, String language) {
        Map<String, Object> templateModel = Map.of(
                "name", username
        );
        String subject = mailMessageSource.getMessage("voterRoleRequestAccepted.subject", null, Locale.of(language));
        htmlEmailService.createHtmlEmail(email, subject, "voterRoleRequestAccepted", templateModel, language);
    }

    public void sendVoterRoleRequestRejectedEmail(String email, String username, String language) {
        Map<String, Object> templateModel = Map.of(
                "name", username
        );
        String subject = mailMessageSource.getMessage("voterRoleRequestRejected.subject", null, Locale.of(language));
        htmlEmailService.createHtmlEmail(email, subject, "voterRoleRequestRejected", templateModel, language);
    }

    @Async
    public void sendVerificationReminderEmail(User user, SensitiveData token) {
        URI uri = URI.create(url + "/resetPassword/" + token.data());
        Map<String, Object> templateModel = Map.of(
                "name", user.getUsername(),
                "uri", uri
        );
        String subject = mailMessageSource.getMessage("verificationReminder.subject", null, Locale.of(user.getLanguage()));
        htmlEmailService.createHtmlEmail(user.getEmail(), subject, "verificationReminder", templateModel, user.getLanguage());
    }
}
