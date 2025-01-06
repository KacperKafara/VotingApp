package pl.kafara.voting.vote.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.vote.ParliamentaryClub;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public interface ParliamentaryClubRepository extends JpaRepository<ParliamentaryClub, String> {

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    List<ParliamentaryClub> findAllByIdNot(String id);

    @NonNull
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    Optional<ParliamentaryClub> findById(@NonNull String id);
}
