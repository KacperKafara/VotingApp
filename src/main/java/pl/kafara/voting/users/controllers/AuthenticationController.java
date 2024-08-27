package pl.kafara.voting.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.messages.GenericMessages;
import pl.kafara.voting.exceptions.user.AccountNotActiveException;
import pl.kafara.voting.exceptions.user.InvalidLoginDataException;
import pl.kafara.voting.users.dto.LoginRequest;
import pl.kafara.voting.users.dto.LoginResponse;
import pl.kafara.voting.users.dto.RegistrationRequest;
import pl.kafara.voting.users.services.AuthenticationService;
import pl.kafara.voting.users.services.EmailService;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @PreAuthorize("permitAll()")
    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authenticate(@Validated @RequestBody LoginRequest loginRequest) throws NotFoundException {
        try {
            return ResponseEntity.ok(new LoginResponse(authenticationService.authenticate(loginRequest)));
        } catch (AccountNotActiveException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (InvalidLoginDataException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Validated @RequestBody RegistrationRequest registrationRequest) throws NotFoundException, NoSuchAlgorithmException {
        String token = authenticationService.register(registrationRequest);
        emailService.sendAccountVerificationEmail(registrationRequest.email(), token, registrationRequest.firstName(), "pl");
        if (token == null || token.isEmpty())
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, GenericMessages.SOMETHING_WENT_WRONG);
        return ResponseEntity.ok().build();
    }
}
