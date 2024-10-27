package pl.kafara.voting.users.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import pl.kafara.voting.exceptions.handlers.ExceptionCodes;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.exceptions.user.OwnRolesModificationException;
import pl.kafara.voting.exceptions.user.UserBlockException;
import pl.kafara.voting.exceptions.user.UserMustHaveAtLeastOneRoleException;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.users.dto.RoleRequest;
import pl.kafara.voting.users.dto.UpdateUserDataRequest;
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


    @PutMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> modifyUserRoles(@Validated @RequestBody RoleRequest roleRequest, @PathVariable UUID userId) throws NotFoundException {
        DecodedJWT jwt =  JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if(jwt.getSubject().equals(userId.toString()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UserMessages.CANNOT_MODIFY_YOURSELF_ROLES, new OwnRolesModificationException(UserMessages.CANNOT_MODIFY_YOURSELF_ROLES, ExceptionCodes.CANNOT_MODIFY_YOURSELF_ROLES));

        try {
            userService.modifyUserRoles(userId, roleRequest.roles());
            return ResponseEntity.ok().build();
        } catch (UserMustHaveAtLeastOneRoleException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/{userId}/block")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> blockUser(@PathVariable UUID userId) throws NotFoundException {
        DecodedJWT jwt =  JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if(jwt.getSubject().equals(userId.toString()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UserMessages.CANNOT_BLOCK_YOURSELF, new UserBlockException(UserMessages.CANNOT_BLOCK_YOURSELF, ExceptionCodes.CANNOT_BLOCK_YOURSELF));

        User user = userService.block(userId);
        emailService.sendAccountBlockedEmail(user.getEmail(), user.getUsername(), user.getLanguage());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/block")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> unblockUser(@PathVariable UUID userId) throws NotFoundException {
        DecodedJWT jwt =  JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if(jwt.getSubject().equals(userId.toString()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UserMessages.CANNOT_UNBLOCK_YOURSELF, new UserBlockException(UserMessages.CANNOT_UNBLOCK_YOURSELF, ExceptionCodes.CANNOT_UNBLOCK_YOURSELF));

        User user = userService.unblock(userId);
        emailService.sendAccountUnblockedEmail(user.getEmail(), user.getUsername(), user.getLanguage());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UserResponse> getUser(@PathVariable String username) throws NotFoundException {
        return ResponseEntity.ok(UserMapper.mapToUserResponse(userService.getUser(username)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UsersResponse> getUsers(@RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "10") int size,
                                                  @RequestParam(name = "username", defaultValue = "") String username,
                                                  @RequestParam(name = "role", defaultValue = "") String role,
                                                  @RequestParam(name = "sort", defaultValue = "asc") @Pattern(regexp = "asc|desc") String sort) {

        Sort sortBy = Sort.by(Sort.Direction.fromString(sort), "username");
        Pageable pageable = PageRequest.of(page, size, sortBy);
        FilteringCriteria filteringCriteria = FilteringCriteria.builder()
                .pageable(pageable)
                .username(username)
                .role(role.toUpperCase())
                .build();

        return ResponseEntity.ok(userService.getUsers(filteringCriteria));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UserResponse> updateUser(@Validated @RequestBody UpdateUserDataRequest userData, @PathVariable UUID userId) throws NotFoundException {
        return ResponseEntity.ok(UserMapper.mapToUserResponse(userService.updateUser(userData, userId)));
    }
}
