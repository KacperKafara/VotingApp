package pl.kafara.voting.users.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.user.WrongPasswordException;
import pl.kafara.voting.users.dto.ChangePasswordRequest;
import pl.kafara.voting.users.dto.UpdateUserDataRequest;
import pl.kafara.voting.users.dto.UserResponse;
import pl.kafara.voting.users.mapper.UserMapper;
import pl.kafara.voting.users.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class MeController {
    private final UserService userService;

    @PatchMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(@Validated @RequestBody ChangePasswordRequest password) throws NotFoundException {
        DecodedJWT jwt =  JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UUID id = UUID.fromString(jwt.getSubject());
        try {
            userService.changePassword(password, id);
            return ResponseEntity.ok().build();
        } catch (WrongPasswordException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getMe() throws NotFoundException {
        DecodedJWT jwt =  JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UUID id = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(UserMapper.mapToUserResponse(userService.getUserById(id)));
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateMe(@Validated @RequestBody UpdateUserDataRequest userData) throws NotFoundException {
        DecodedJWT jwt =  JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UUID id = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(UserMapper.mapToUserResponse(userService.updateUser(userData, id)));
    }
}
