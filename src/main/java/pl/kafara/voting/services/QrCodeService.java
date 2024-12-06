package pl.kafara.voting.services;

import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.exceptionCodes.UserExceptionCodes;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.exceptions.user.TotpAuthorisationException;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.util.AESUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class QrCodeService {
    private final UserRepository userRepository;
    private final AESUtils aesUtils;

    @PreAuthorize("hasRole('VOTER')")
    public byte[] generateQrCodeForVoting(UUID id) throws NotFoundException, QrGenerationException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_NOT_FOUND));
        QrData qrData = new QrData.Builder()
                .label("Voting:" + user.getUsername())
                .secret(aesUtils.decrypt(user.getTotpSecret()))
                .issuer("Voting")
                .digits(6)
                .period(30)
                .build();
        QrGenerator qrGenerator = new ZxingPngQrGenerator();
        return qrGenerator.generate(qrData);
    }

    @PreAuthorize("isAuthenticated()")
    public byte[] generateQrCodeForAuthorisation(UUID id) throws NotFoundException, QrGenerationException, TotpAuthorisationException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_NOT_FOUND));
        if(user.getAuthorisationTotpSecret() == null) {
            throw new TotpAuthorisationException(UserMessages.FIRST_ACTIVATE_2FA, UserExceptionCodes.FIRST_ACTIVATE_2FA);
        }
        QrData qrData = new QrData.Builder()
                .label("Authorisation:" + user.getUsername())
                .secret(aesUtils.decrypt(user.getAuthorisationTotpSecret()))
                .issuer("Voting")
                .digits(6)
                .period(30)
                .build();
        QrGenerator qrGenerator = new ZxingPngQrGenerator();
        return qrGenerator.generate(qrData);
    }
}
