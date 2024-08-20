package pl.kafara.voting.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kafara.voting.model.users.Gender;
import pl.kafara.voting.model.users.GenderEnum;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenderRepository extends JpaRepository<Gender, UUID> {
    Optional<Gender> findByName(GenderEnum name);
}
