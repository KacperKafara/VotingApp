package pl.kafara.voting.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.user.VerificationTokenExpiredException;
import pl.kafara.voting.exceptions.user.VerificationTokenUsedException;
import pl.kafara.voting.users.services.VerificationService;
import pl.kafara.voting.util.SensitiveData;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class VerificationController {

    private final VerificationService verificationService;

    @PreAuthorize("permitAll()")
    @PostMapping("/{token}")
    public ResponseEntity<Void> verify(@PathVariable String token) throws NotFoundException {
        try {
            verificationService.verify(new SensitiveData(token));
            return ResponseEntity.ok().build();
        } catch (VerificationTokenUsedException | VerificationTokenExpiredException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
