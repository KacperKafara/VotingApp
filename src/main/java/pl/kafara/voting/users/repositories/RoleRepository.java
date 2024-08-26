package pl.kafara.voting.users.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.users.Role;
import pl.kafara.voting.model.users.UserRoleEnum;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface RoleRepository extends JpaRepository<Role, UUID> {
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    Optional<Role> findByName(@NonNull UserRoleEnum name);
}
