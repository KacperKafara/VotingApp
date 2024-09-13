package pl.kafara.voting.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.messages.GenericMessages;
import pl.kafara.voting.exceptions.user.AccountNotActiveException;
import pl.kafara.voting.exceptions.user.VerificationTokenExpiredException;
import pl.kafara.voting.exceptions.user.VerificationTokenUsedException;
import pl.kafara.voting.users.dto.ResetPasswordFormRequest;
import pl.kafara.voting.users.dto.ResetPasswordRequest;
import pl.kafara.voting.users.services.EmailService;
import pl.kafara.voting.users.services.TokenService;
import pl.kafara.voting.users.services.UserService;
import pl.kafara.voting.util.SensitiveData;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/resetPassword")
@Transactional(propagation = Propagation.NEVER)
@RequiredArgsConstructor
public class ResetPasswordController {
    private final UserService userService;
    private final EmailService emailService;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<Void> resetPassword(@Validated @RequestBody ResetPasswordRequest resetPasswordRequest) throws NotFoundException, NoSuchAlgorithmException {
        try {
            SensitiveData token = userService.resetPassword(resetPasswordRequest.email());
            if (token == null || token.data().isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GenericMessages.TOKEN_CANNOT_BE_BLANK);
            emailService.sendResetPasswordEmail(resetPasswordRequest.email(), token, resetPasswordRequest.language());
            return ResponseEntity.ok().build();
        } catch (AccountNotActiveException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PostMapping("/{token}/verify")
    public ResponseEntity<Void> verifyToken(@PathVariable String token) {
        if(tokenService.isResetPasswordTokenValid(token))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{token}")
    public ResponseEntity<Void> resetPassword(@PathVariable String token, @Validated @RequestBody ResetPasswordFormRequest resetPasswordRequest) throws NotFoundException {
        if(tokenService.isResetPasswordTokenValid(token)) {
            try {
                userService.resetPassword(token, resetPasswordRequest);
                return ResponseEntity.ok().build();
            } catch (VerificationTokenUsedException | VerificationTokenExpiredException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
