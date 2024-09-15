package pl.kafara.voting.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.user.UserMustHaveAtLeastOneRoleException;
import pl.kafara.voting.users.dto.RoleRequest;
import pl.kafara.voting.users.services.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class UserController {
    private final UserService userService;

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
}
