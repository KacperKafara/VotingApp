package pl.kafara.voting.vote.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.vote.Voting;
import pl.kafara.voting.model.vote.VotingOption;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface VotingOptionRepository extends JpaRepository<VotingOption, UUID> {

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    Optional<VotingOption> findByIdAndVoting(UUID id, Voting voting);
}
