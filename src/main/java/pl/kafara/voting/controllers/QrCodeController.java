package pl.kafara.voting.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.samstevens.totp.exceptions.QrGenerationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.user.TotpAuthorisationException;
import pl.kafara.voting.services.QrCodeService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qr-code")
@Transactional(propagation = Propagation.NEVER)
public class QrCodeController {
    private final QrCodeService qrCodeService;

    @GetMapping(produces = "image/png")
    @PreAuthorize("hasRole('VOTER')")
    public ResponseEntity<byte[]> getQrCode() throws NotFoundException, QrGenerationException {
        DecodedJWT jwt =  JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UUID id = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(qrCodeService.generateQrCodeForVoting(id));
    }

    @GetMapping(value = "/authorisation", produces = "image/png")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> getAuthorisationQrCode() throws NotFoundException, QrGenerationException {
        DecodedJWT jwt =  JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UUID id = UUID.fromString(jwt.getSubject());
        try {
            return ResponseEntity.ok(qrCodeService.generateQrCodeForAuthorisation(id));
        } catch (TotpAuthorisationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
