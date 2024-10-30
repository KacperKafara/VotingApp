package pl.kafara.voting.vote.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kafara.voting.vote.api.model.LastVotingsUpdate;

@Repository
public interface LastVotingsUpdateRepository extends JpaRepository<LastVotingsUpdate, Long> {

    LastVotingsUpdate findById(long id);
}
