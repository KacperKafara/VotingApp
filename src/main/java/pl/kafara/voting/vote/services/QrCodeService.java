package pl.kafara.voting.vote.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.exceptionCodes.UserExceptionCodes;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.util.AESUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class QrCodeService {
    private final UserRepository userRepository;
    private final AESUtils aesUtils;

    @PreAuthorize("hasRole('VOTER')")
    public byte[] generateQrCode(UUID id) throws NotFoundException, WriterException, IOException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(UserMessages.USER_NOT_FOUND, UserExceptionCodes.USER_NOT_FOUND));
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String secret = aesUtils.decrypt(user.getTotpSecret());
        String otpAuthURL = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", "Voting", user.getUsername(), secret, "Voting");
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 400, 400);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}
