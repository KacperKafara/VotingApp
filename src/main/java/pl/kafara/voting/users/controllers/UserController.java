package pl.kafara.voting.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.user.UserBlockException;
import pl.kafara.voting.exceptions.user.UserMustHaveAtLeastOneRoleException;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.users.dto.RoleRequest;
import pl.kafara.voting.users.dto.UserResponse;
import pl.kafara.voting.users.dto.UsersResponse;
import pl.kafara.voting.users.mapper.UserMapper;
import pl.kafara.voting.users.services.EmailService;
import pl.kafara.voting.users.services.UserService;
import pl.kafara.voting.util.FilteringCriteria;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

    @PutMapping("/role")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> addRole(@Validated @RequestBody RoleRequest roleRequest) throws NotFoundException {
        userService.addRole(roleRequest.userId(), roleRequest.role());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/role")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> removeRole(@Validated @RequestBody RoleRequest roleRequest) throws NotFoundException {
        try {
            userService.removeRole(roleRequest.userId(), roleRequest.role());
            return ResponseEntity.ok().build();
        } catch (UserMustHaveAtLeastOneRoleException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/block/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> blockUser(@PathVariable UUID userId) throws NotFoundException {
        try {
            User user = userService.block(userId);
            emailService.sendAccountBlockedEmail(user.getEmail(), user.getUsername(), user.getLanguage());
            return ResponseEntity.ok().build();
        } catch (UserBlockException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/block/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> unblockUser(@PathVariable UUID userId) throws NotFoundException {
        try {
            User user = userService.unblock(userId);
            emailService.sendAccountUnblockedEmail(user.getEmail(), user.getUsername(), user.getLanguage());
            return ResponseEntity.ok().build();
        } catch (UserBlockException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) throws NotFoundException {
        return ResponseEntity.ok(UserMapper.mapToUserResponse(userService.getUser(userId)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UsersResponse> getUsers(@RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "10") int size,
                                                  @RequestParam(name = "username", defaultValue = "") String username,
                                                  @RequestParam(name = "role", defaultValue = "") String role,
                                                  @RequestParam(name = "sort", defaultValue = "username") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        FilteringCriteria filteringCriteria = FilteringCriteria.builder()
                .pageable(pageable)
                .username(username)
                .role(role.toUpperCase())
                .build();

        return ResponseEntity.ok(userService.getUsers(filteringCriteria));
    }
}
