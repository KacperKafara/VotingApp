package pl.kafara.voting.vote.api.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.vote.LastVotingsUpdate;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public interface LastVotingsUpdateRepository extends JpaRepository<LastVotingsUpdate, Long> {

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @NonNull Optional<LastVotingsUpdate> findById(@NonNull Long id);

    @Transactional(propagation = Propagation.REQUIRED)
    @NonNull LastVotingsUpdate save(@NonNull LastVotingsUpdate lastVotingsUpdate);
}
