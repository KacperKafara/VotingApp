package pl.kafara.voting.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.kafara.voting.exceptions.CreationException;
import pl.kafara.voting.exceptions.IdenticalFieldValueException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.user.AccountNotActiveException;
import pl.kafara.voting.exceptions.user.InvalidLoginDataException;
import pl.kafara.voting.users.dto.LoginRequest;
import pl.kafara.voting.users.dto.LoginResponse;
import pl.kafara.voting.users.dto.RegistrationRequest;
import pl.kafara.voting.users.services.AuthenticationService;

@RestController
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PreAuthorize("permitAll()")
    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authenticate(@Validated @RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(new LoginResponse(authenticationService.authenticate(loginRequest)));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (AccountNotActiveException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (InvalidLoginDataException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Validated @RequestBody RegistrationRequest registrationRequest) {
        try {
            authenticationService.register(registrationRequest);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<String> test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return ResponseEntity.ok("OK");
    }
}
