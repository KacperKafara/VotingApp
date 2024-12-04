package pl.kafara.voting.users.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.users.Role;
import pl.kafara.voting.model.users.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface UserRepository extends JpaRepository<User, UUID> {
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    Optional<User> findByUsername(String username);
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    Optional<User> findByEmail(String email);
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    List<User> getUsersByCreatedAtBeforeAndVerifiedIsFalse(LocalDateTime createdAt);
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    List<User> getUsersByCreatedAtBeforeAndCreatedAtAfterAndVerifiedIsFalse(LocalDateTime createdAtBefore, LocalDateTime createdAtAfter);
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    Page<User> getAllByUsernameContains(Pageable pageable, String username);
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    Page<User> getAllByUsernameContainsAndRolesContaining(Pageable pageable, String username, Role role);
}
