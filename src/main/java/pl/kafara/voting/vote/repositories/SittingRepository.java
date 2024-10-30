package pl.kafara.voting.vote.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.kafara.voting.model.vote.Sitting;
import pl.kafara.voting.vote.api.model.LastVotingsUpdate;

import java.util.List;

@Repository
public interface SittingRepository extends JpaRepository<Sitting, Integer> {

    @Query("SELECT s FROM Sitting s WHERE s.number >= :lastVotingsUpdateNumber")
    List<Sitting> findByNumber(Long lastVotingsUpdateNumber);
}
