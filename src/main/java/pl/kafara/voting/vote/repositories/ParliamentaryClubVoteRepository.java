package pl.kafara.voting.vote.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.vote.survey.UserVoteParliamentaryClub;

import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface ParliamentaryClubVoteRepository extends JpaRepository<UserVoteParliamentaryClub, UUID> {
}
