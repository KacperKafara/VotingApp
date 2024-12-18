package pl.kafara.voting.users.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.server.ResponseStatusException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.exceptionCodes.UserExceptionCodes;
import pl.kafara.voting.exceptions.messages.GenericMessages;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.exceptions.user.AccountNotActiveException;
import pl.kafara.voting.exceptions.user.InvalidLoginDataException;
import pl.kafara.voting.exceptions.user.MFARequiredException;
import pl.kafara.voting.users.dto.*;
import pl.kafara.voting.users.services.AuthenticationService;
import pl.kafara.voting.users.services.EmailService;
import pl.kafara.voting.util.SensitiveData;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @PreAuthorize("permitAll()")
    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authenticate(@Validated @RequestBody LoginRequest loginRequest) {
        try {
            Map<String, SensitiveData> data = authenticationService.authenticate(loginRequest);
            LoginResponse loginResponse = new LoginResponse(data.get("token").data(), data.get("refreshToken").data());
            return ResponseEntity.ok(loginResponse);
        } catch (AccountNotActiveException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (InvalidLoginDataException | NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (MFARequiredException e) {
            throw new ResponseStatusException(HttpStatus.OK, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/verifyTotp")
    public ResponseEntity<LoginResponse> verifyTotp(@Validated @RequestBody TotpLoginRequest loginRequest) {
        try {
            Map<String, SensitiveData> data = authenticationService.verifyTotp(loginRequest);
            LoginResponse loginResponse = new LoginResponse(data.get("token").data(), data.get("refreshToken").data());
            return ResponseEntity.ok(loginResponse);
        } catch (AccountNotActiveException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (InvalidLoginDataException | NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Validated @RequestBody RegistrationRequest registrationRequest) throws NotFoundException, NoSuchAlgorithmException {
        SensitiveData token = authenticationService.register(registrationRequest);
        if (token == null || token.data().isEmpty())
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, GenericMessages.SOMETHING_WENT_WRONG);
        emailService.sendAccountVerificationEmail(registrationRequest.email(), token, registrationRequest.firstName(), registrationRequest.language());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody SensitiveData refreshToken) {
        try {
            Map<String, SensitiveData> data = authenticationService.refresh(refreshToken);
            LoginResponse loginResponse = new LoginResponse(data.get("token").data(), data.get("refreshToken").data());
            return ResponseEntity.ok(loginResponse);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (JWTVerificationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UserMessages.REFRESH_TOKEN_VERIFICATION_EXCEPTION, new InvalidLoginDataException(UserMessages.REFRESH_TOKEN_VERIFICATION_EXCEPTION, UserExceptionCodes.INVALID_REFRESH_TOKEN));
        }
    }
}
