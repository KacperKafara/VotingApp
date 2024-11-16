package pl.kafara.voting.vote.api.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.vote.Sitting;

import java.util.List;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface SittingRepository extends JpaRepository<Sitting, Integer> {

    @Query("SELECT s FROM Sitting s WHERE s.number >= :lastVotingsUpdateNumber")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    List<Sitting> findByNumber(Long lastVotingsUpdateNumber);

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Nonnull
    List<Sitting> findAll();
}
