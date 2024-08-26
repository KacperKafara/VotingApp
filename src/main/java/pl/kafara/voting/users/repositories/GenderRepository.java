package pl.kafara.voting.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.users.Gender;
import pl.kafara.voting.model.users.GenderEnum;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface GenderRepository extends JpaRepository<Gender, UUID> {
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    Optional<Gender> findByName(GenderEnum name);
}
