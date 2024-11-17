package pl.kafara.voting.services;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.secret.SecretGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TotpService {
    private final CodeVerifier codeVerifier;
    private final SecretGenerator secretGenerator;

    public String generateSecret() {
        return secretGenerator.generate();
    }

    public boolean verifyCode(String secret, String code) {
        return codeVerifier.isValidCode(secret, code);
    }
}
