package pl.kafara.voting.vote.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.vote.Sitting;
import pl.kafara.voting.model.vote.Voting;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface VotingRepository extends JpaRepository<Voting, UUID> {

    @Query("SELECT v FROM Voting v WHERE v.sittingDay = :sittingDay AND v.votingNumber = :votingNumber AND v.sitting = :sitting")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    Optional<Voting> getVotingFiltered(int sittingDay, int votingNumber, Sitting sitting);

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    Page<Voting> getVotingByTitleContains(Pageable pageable, String title);

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    Page<Voting> getVotingByTitleContainsAndSitting(Pageable pageable, String title, Sitting sitting);

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    @Query("SELECT v FROM Voting v WHERE v.endDate > CURRENT_TIMESTAMP")
    List<Voting> getAllByEndDateAfterNow();
}
