package pl.kafara.voting.vote.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kafara.voting.model.vote.Envoy;

@Repository
public interface EnvoyRepository extends JpaRepository<Envoy, Long> {
}
