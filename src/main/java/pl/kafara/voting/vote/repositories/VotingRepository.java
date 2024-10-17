package pl.kafara.voting.vote.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.kafara.voting.model.vote.Sitting;
import pl.kafara.voting.model.vote.Voting;

import java.util.Optional;

@Repository
public interface VotingRepository extends JpaRepository<Voting, Long> {
    @Query("SELECT v FROM Voting v WHERE v.sittingDay = :sittingDay AND v.votingNumber = :votingNumber AND v.sitting = :sitting")
    Optional<Voting> getVotingFiltered(int sittingDay, int votingNumber, Sitting sitting);
}
