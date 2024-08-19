package pl.kafara.voting.users.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.kafara.voting.model.users.Role;
import pl.kafara.voting.model.users.UserRoleEnum;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(@NonNull UserRoleEnum name);
}
