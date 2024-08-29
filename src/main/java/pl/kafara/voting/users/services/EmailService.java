package pl.kafara.voting.users.services;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.services.HtmlEmailService;

import java.net.URI;
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

    public void sendAccountVerificationEmail(String to, String token, String name, String lang) {
        URI uri = URI.create(url + "/verify/" + token);
        Map<String, Object> templateModel = Map.of(
                "name", name,
                "uri", uri);
        String subject = mailMessageSource.getMessage("accountVerification.subject", null, Locale.of(lang));
        htmlEmailService.createHtmlEmail(to, subject, "accountVerification", templateModel, lang);
    }

    public void sendResetPasswordEmail(String email, String token, String lang) {
        URI uri = URI.create(url + "/resetPassword/" + token);
        Map<String, Object> templateModel = Map.of(
                "uri", uri);
        String subject = mailMessageSource.getMessage("resetPassword.subject", null, Locale.of(lang));
        htmlEmailService.createHtmlEmail(email, subject, "resetPassword", templateModel, lang);

    }
}
