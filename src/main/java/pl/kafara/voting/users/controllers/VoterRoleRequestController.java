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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.user.ResolveOwnRequestException;
import pl.kafara.voting.exceptions.user.RoleRequestException;
import pl.kafara.voting.exceptions.user.YouAreVoterException;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.users.dto.VoterRoleRequestListResponse;
import pl.kafara.voting.users.services.EmailService;
import pl.kafara.voting.users.services.VoterRoleRequestService;
import pl.kafara.voting.util.filteringCriterias.RoleRequestFilteringCriteria;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voterRoleRequest")
@Transactional(propagation = Propagation.NEVER)
public class VoterRoleRequestController {
    private final VoterRoleRequestService voterRoleRequestService;
    private final EmailService emailService;

    @PostMapping
    @PreAuthorize("hasRole('USER') and !hasRole('VOTER')")
    public ResponseEntity<Void> createRequest() throws NotFoundException {
        DecodedJWT jwt = JWT.decode((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UUID id = UUID.fromString(jwt.getSubject());
        try {
            voterRoleRequestService.createRequest(id);
            return ResponseEntity.ok().build();
        } catch (YouAreVoterException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> acceptRequest(@PathVariable UUID id) throws NotFoundException {
        try {
            User user = voterRoleRequestService.acceptRequest(id);
            emailService.sendVoterRoleRequestAcceptedEmail(user.getEmail(), user.getUsername(), user.getLanguage());
            return ResponseEntity.ok().build();
        } catch (ResolveOwnRequestException | RoleRequestException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> rejectRequest(@PathVariable UUID id) throws NotFoundException {
        try {
            User user = voterRoleRequestService.rejectRequest(id);
            emailService.sendVoterRoleRequestRejectedEmail(user.getEmail(), user.getUsername(), user.getLanguage());
            return ResponseEntity.ok().build();
        } catch (ResolveOwnRequestException | RoleRequestException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<VoterRoleRequestListResponse> getRoleRequests(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "username", defaultValue = "") String username,
            @RequestParam(name = "sort", defaultValue = "asc") @Pattern(regexp = "asc|desc") String sort
    ) {
        Sort sortBy = Sort.by(Sort.Direction.fromString(sort), "requestDate");
        Pageable pageable = PageRequest.of(page, size, sortBy);

        RoleRequestFilteringCriteria filteringCriteria = RoleRequestFilteringCriteria.builder()
                .pageable(pageable)
                .username(username)
                .build();

        return ResponseEntity.ok(voterRoleRequestService.getRoleRequests(filteringCriteria));
    }
}
