package pl.kafara.voting.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.messages.GenericMessages;
import pl.kafara.voting.exceptions.user.AccountNotActiveException;
import pl.kafara.voting.users.dto.ResetPasswordRequest;
import pl.kafara.voting.users.services.EmailService;
import pl.kafara.voting.users.services.UserService;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/users")
@Transactional(propagation = Propagation.NEVER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/resetPassword")
    public ResponseEntity<Void> resetPassword(@Validated @RequestBody ResetPasswordRequest resetPasswordRequest) throws NotFoundException, NoSuchAlgorithmException {
        try {
            String token = userService.resetPassword(resetPasswordRequest.email());
            if (token == null || token.isEmpty())
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, GenericMessages.SOMETHING_WENT_WRONG);
            emailService.sendResetPasswordEmail(resetPasswordRequest.email(), token, resetPasswordRequest.language());
            return ResponseEntity.ok().build();
        } catch (AccountNotActiveException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }
}
