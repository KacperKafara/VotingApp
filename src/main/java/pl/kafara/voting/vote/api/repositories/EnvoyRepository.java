package pl.kafara.voting.vote.api.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.vote.Envoy;
import pl.kafara.voting.model.vote.ParliamentaryClub;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface EnvoyRepository extends JpaRepository<Envoy, UUID> {

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @NonNull Envoy save(@NonNull Envoy envoy);

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @NonNull Optional<Envoy> findById(@NonNull UUID id);

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    Optional<Envoy> findByInTermNumberAndClub(Long inTermNumber, ParliamentaryClub club);

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Query("SELECT e FROM Envoy e WHERE e.inTermNumber = :inTermNumber AND e.club.term = :term")
    Optional<Envoy> findByInTermNumberAndTerm(Long inTermNumber, String term);
}
