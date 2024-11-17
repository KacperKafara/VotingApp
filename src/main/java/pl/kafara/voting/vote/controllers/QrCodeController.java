package pl.kafara.voting.vote.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.vote.services.QrCodeService;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qr-code")
@Transactional(propagation = Propagation.NEVER)
public class QrCodeController {
    private final QrCodeService qrCodeService;

    @GetMapping(produces = "image/png")
    @PreAuthorize("hasRole('VOTER')")
    public ResponseEntity<byte[]> getQrCode() throws NotFoundException, IOException, WriterException {
        DecodedJWT jwt =  JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UUID id = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(qrCodeService.generateQrCode(id));
    }
}
