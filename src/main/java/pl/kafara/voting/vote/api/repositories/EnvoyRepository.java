package pl.kafara.voting.vote.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.vote.Envoy;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface EnvoyRepository extends JpaRepository<Envoy, Long> {
}
