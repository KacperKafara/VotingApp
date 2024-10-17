package pl.kafara.voting.vote.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kafara.voting.model.vote.ParliamentaryClub;

@Repository
public interface ParliamentaryClubRepository extends JpaRepository<ParliamentaryClub, String> {
}
