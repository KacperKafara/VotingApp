package pl.kafara.voting.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kafara.voting.model.vote.Sitting;

@Repository
public interface SittingRepository extends JpaRepository<Sitting, Integer> {
}
