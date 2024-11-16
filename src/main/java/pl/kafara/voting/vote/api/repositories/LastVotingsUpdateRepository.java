package pl.kafara.voting.vote.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.vote.api.model.LastVotingsUpdate;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public interface LastVotingsUpdateRepository extends JpaRepository<LastVotingsUpdate, Long> {

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    LastVotingsUpdate findById(long id);
}
